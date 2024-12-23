public class CodeAnalyzer {
    public AnalysisResults analyzeCode(String code) {
        String[] lines = code.split("\n");
        int totalLines = lines.length;
        int functionalLines = 0;

        for (String line : lines) {
            if (isJavaFunctionalCode(line)) {
                functionalLines++;
            }
        }

        double functionalPercentage = (double) functionalLines / totalLines * 100;
        
        LOGGER.info("Percentage of code that will work : {}",functionalPercentage); 
    }

   private boolean isJavaFunctionalCode(String line) {
    // Remove leading/trailing whitespace
    line = line.trim();

    // Check if the line is empty or a comment
    if (line.isEmpty() || line.startsWith("//") || line.startsWith("/*") || line.startsWith("*")) {
        return false;
    }

    // Check for method declarations
    if (line.matches("^[a-zA-Z0-9_]+\\s+[a-zA-Z0-9_]+\\s*\\([^)]*\\)\\s*\\{.*")) {
        return true;
    }

    // Check for variable declarations
    if (line.matches("^[a-zA-Z0-9_]+\\s+[a-zA-Z0-9_]+\\s*[=;].*")) {
        return true;
    }

    // Check for class declarations
    if (line.matches("^(public|private|protected|static|final|abstract|class)\\s+[a-zA-Z0-9_]+\\s*\\{.*")) {
        return true;
    }

    // Check for if/else statements
    if (line.matches("^if\\s*\\(.*\\)\\s*\\{.*")) {
        return true;
    }

    // Check for loop statements
    if (line.matches("^(for|while|do)\\s*\\(.*\\)\\s*\\{.*")) {
        return true;
    }

    // Check for return statements
    if (line.matches("^return\\s+.*")) {
        return true;
    }

    // Check for method calls
    if (line.matches("^[a-zA-Z0-9_]+\\s*\\(.*\\)\\s*;.*")) {
        return true;
    }

    // If none of the above checks match, consider the line as non-functional
    return false;
}
}
