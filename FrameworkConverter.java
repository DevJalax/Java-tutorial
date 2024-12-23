import java.util.Scanner;

public class FrameworkConverter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the input framework name: ");
        String inputFramework = scanner.nextLine();
        System.out.print("Enter the output framework name: ");
        String outputFramework = scanner.nextLine();
        System.out.print("Enter the project structure: ");
        String projectStructure = scanner.nextLine();

        String outputStructure = convertProjectStructure(inputFramework, outputFramework, projectStructure);
        System.out.println("Output project structure for " + outputFramework + ":");
        System.out.println(outputStructure);
    }

    public static String convertProjectStructure(String inputFramework, String outputFramework, String projectStructure) {
        switch (inputFramework.toLowerCase()) {
            case "django":
                return convertDjangoToRocket(projectStructure);
            default:
                return "Unsupported input framework: " + inputFramework;
        }
    }

    private static String convertDjangoToRocket(String projectStructure) {
        StringBuilder outputStructure = new StringBuilder();
        String[] lines = projectStructure.split("\n");
        for (String line : lines) {
            if (line.startsWith("├── ") || line.startsWith("│   ")) {
                line = line.replaceAll("├── ", "").replaceAll("│   ", "    ");
                outputStructure.append("src/").append(line).append("\n");
            } else if (line.startsWith("└── ")) {
                line = line.replaceAll("└── ", "").replaceAll("│   ", "    ");
                outputStructure.append("src/").append(line).append("\n");
            } else if (!line.isEmpty()) {
                outputStructure.append(line).append("\n");
            }
        }
        return outputStructure.toString();
    }
}
