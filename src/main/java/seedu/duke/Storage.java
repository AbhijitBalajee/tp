// @@author Ariff1422
package seedu.duke;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Handles saving and loading of expenses and budget to/from an encrypted file.
 * Uses AES-128-CBC encryption with a machine-derived key so that the file
 * cannot be read or tampered with outside of this application.
 */
public class Storage {

    private static final Logger logger = Logger.getLogger(Storage.class.getName());
    private static final String EXPENSES_MARKER = "---EXPENSES---";
    private static final String BUDGET_MARKER = "---BUDGET---";
    private static final String BUDGET_HISTORY_MARKER = "---BUDGET-HISTORY---";
    private static final String GOAL_MARKER = "---GOAL---";
    private static final String CIPHER_ALGO = "AES/CBC/PKCS5Padding";
    private static final int IV_LENGTH = 16;

    static {
        logger.setUseParentHandlers(false);
    }

    private final String filePath;

    /**
     * Constructs a Storage instance for the given file path.
     *
     * @param filePath the path to the save file
     */
    public Storage(String filePath) {
        assert filePath != null && !filePath.isBlank() : "File path should not be null or blank";
        this.filePath = filePath;
    }

    /**
     * Derives a 128-bit AES key from machine-specific properties (OS name and username).
     * The key is unique per machine but reproducible across runs on the same machine.
     *
     * @return a SecretKeySpec for AES encryption
     */
    private SecretKeySpec deriveKey() {
        try {
            String machineId = System.getProperty("os.name", "unknown")
                    + "|" + System.getProperty("user.name", "user");
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(machineId.getBytes(StandardCharsets.UTF_8));
            byte[] keyBytes = Arrays.copyOf(hash, 16);
            return new SecretKeySpec(keyBytes, "AES");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    /**
     * Encrypts a plain-text string using AES-128-CBC with a random IV.
     * Returns a Base64-encoded string: [IV (16 bytes)] + [ciphertext].
     *
     * @param plainText the text to encrypt
     * @return Base64-encoded encrypted data
     */
    private String encrypt(String plainText) {
        try {
            SecretKeySpec key = deriveKey();
            byte[] iv = new byte[IV_LENGTH];
            new SecureRandom().nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance(CIPHER_ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            byte[] cipherBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            byte[] combined = new byte[IV_LENGTH + cipherBytes.length];
            System.arraycopy(iv, 0, combined, 0, IV_LENGTH);
            System.arraycopy(cipherBytes, 0, combined, IV_LENGTH, cipherBytes.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    /**
     * Decrypts a Base64-encoded AES-128-CBC encrypted string.
     * Returns null if decryption fails (e.g. file was tampered with).
     *
     * @param encryptedText Base64-encoded encrypted data
     * @return decrypted plain text, or null if decryption fails
     */
    private String decrypt(String encryptedText) {
        try {
            byte[] combined = Base64.getDecoder().decode(encryptedText.trim());
            if (combined.length <= IV_LENGTH) {
                return null;
            }
            byte[] iv = Arrays.copyOf(combined, IV_LENGTH);
            byte[] cipherBytes = Arrays.copyOfRange(combined, IV_LENGTH, combined.length);

            SecretKeySpec key = deriveKey();
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance(CIPHER_ALGO);
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
            byte[] plainBytes = cipher.doFinal(cipherBytes);

            return new String(plainBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Saves all expenses and budget to disk as an encrypted file.
     * Creates the data directory if it does not exist.
     * Prints a warning and continues if the write fails.
     *
     * @param expenses the expense list to save
     */
    public void save(ExpenseList expenses) {
        assert expenses != null : "ExpenseList to save should not be null";

        File file = new File(filePath);
        file.getParentFile().mkdirs();

        StringBuilder sb = new StringBuilder();
        sb.append(EXPENSES_MARKER).append("\n");
        for (int i = 0; i < expenses.size(); i++) {
            Expense e = expenses.getExpense(i);
            sb.append(e.getDescription()).append("|")
                    .append(e.getAmount()).append("|")
                    .append(e.getCategory()).append("|")
                    .append(e.getDate()).append("|")
                    .append(e.isRecurring()).append("\n");
        }
        sb.append(BUDGET_MARKER).append("\n");
        sb.append(expenses.getBudget()).append("\n");
        sb.append(BUDGET_HISTORY_MARKER).append("\n");
        for (String entry : expenses.getBudgetHistory()) {
            sb.append(entry).append("\n");
        }
        // @@author pranavjana
        sb.append(GOAL_MARKER).append("\n");
        sb.append(expenses.getGoal()).append("\n");
        // @@author Ariff1422

        try (FileWriter fw = new FileWriter(file)) {
            fw.write(encrypt(sb.toString()));
            logger.info("Saved and encrypted " + expenses.size() + " expenses to " + filePath);
        } catch (IOException e) {
            System.out.println("Warning: could not save data. " + e.getMessage());
            logger.warning("Save failed: " + e.getMessage());
        }
    }

    /**
     * Loads expenses and budget from the encrypted file into the given expense list.
     * If the file is missing, starts fresh silently.
     * If the file cannot be decrypted (tampered or from a different machine), rejects it
     * entirely and starts fresh with a warning.
     *
     * @param expenses the expense list to populate
     */
    public void load(ExpenseList expenses) {
        assert expenses != null : "ExpenseList to load into should not be null";

        File file = new File(filePath);
        if (!file.exists()) {
            logger.info("No save file found at " + filePath + ". Starting fresh.");
            return;
        }

        String encryptedContent;
        try {
            encryptedContent = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Warning: could not load data. " + e.getMessage());
            logger.warning("Load failed: " + e.getMessage());
            return;
        }

        String plainText = decrypt(encryptedContent);
        if (plainText == null) {
            System.out.println("Warning: save file could not be decrypted. "
                    + "It may have been tampered with or created on a different machine. "
                    + "Starting fresh.");
            logger.warning("Decryption failed for file: " + filePath);
            return;
        }

        try (Scanner sc = new Scanner(plainText)) {
            boolean readingBudget = false;
            boolean readingHistory = false;
            boolean readingGoal = false;
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.equals(EXPENSES_MARKER)) {
                    readingBudget = false;
                    readingHistory = false;
                    readingGoal = false;
                    continue;
                }
                if (line.equals(BUDGET_MARKER)) {
                    readingBudget = true;
                    readingHistory = false;
                    readingGoal = false;
                    continue;
                }
                if (line.equals(BUDGET_HISTORY_MARKER)) {
                    readingBudget = false;
                    readingHistory = true;
                    readingGoal = false;
                    continue;
                }
                // @@author pranavjana
                if (line.equals(GOAL_MARKER)) {
                    readingBudget = false;
                    readingHistory = false;
                    readingGoal = true;
                    continue;
                }
                if (readingGoal) {
                    try {
                        double goal = Double.parseDouble(line);
                        if (goal > 0) {
                            expenses.setGoal(goal);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Warning: could not parse goal value: " + line);
                    }
                    readingGoal = false;
                    continue;
                }
                // @@author Ariff1422
                if (readingBudget) {
                    try {
                        double budget = Double.parseDouble(line);
                        if (budget > 0) {
                            expenses.setBudgetDirectly(budget);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Warning: could not parse budget value: " + line);
                    }
                    readingBudget = false;
                    continue;
                }
                if (readingHistory) {
                    if (!line.isBlank()) {
                        expenses.addBudgetHistory(line);
                    }
                    continue;
                }
                parseLine(line, expenses);
            }
            logger.info("Loaded " + expenses.size() + " expenses from " + filePath);
        }
    }

    private void parseLine(String line, ExpenseList expenses) {
        String[] parts = line.split("\\|");
        if (parts.length < 4 || parts.length > 5) {
            System.out.println("Warning: skipping malformed line: " + line);
            return;
        }
        try {
            String description = parts[0];
            double amount = Double.parseDouble(parts[1]);
            String category = parts[2];
            LocalDate date = LocalDate.parse(parts[3]);
            boolean recurring = parts.length == 5 && Boolean.parseBoolean(parts[4]);

            // @@author Ariff1422
            if (!validateExpense(description, amount, date)) {
                return;
            }
            // @@author

            expenses.addExpense(new Expense(description, amount, category, date, recurring));
        } catch (Exception e) {
            System.out.println("Warning: skipping malformed line: " + line);
            logger.warning("Failed to parse line: " + line);
        }
    }

    // @@author Ariff1422
    /**
     * Validates an expense's fields after parsing from the save file.
     * Warns and returns false if any field is invalid, so the line is skipped.
     *
     * @param description the expense description
     * @param amount      the expense amount
     * @param date        the expense date
     * @return true if all fields are valid, false otherwise
     */
    private boolean validateExpense(String description, double amount, LocalDate date) {
        if (description == null || description.isBlank()) {
            System.out.println("Warning: skipping expense with blank description.");
            logger.warning("Skipping expense with blank description.");
            return false;
        }
        if (amount <= 0) {
            System.out.println("Warning: skipping expense with non-positive amount: " + amount);
            logger.warning("Skipping expense with non-positive amount: " + amount);
            return false;
        }
        if (amount > 1000000) {
            System.out.println("Warning: skipping expense with unrealistic amount: " + amount);
            logger.warning("Skipping expense with unrealistic amount: " + amount);
            return false;
        }
        if (date != null && date.getYear() < 2000) {
            System.out.println("Warning: skipping expense with implausible date: " + date);
            logger.warning("Skipping expense with implausible date: " + date);
            return false;
        }
        return true;
    }
    // @@author
}
// @@author
