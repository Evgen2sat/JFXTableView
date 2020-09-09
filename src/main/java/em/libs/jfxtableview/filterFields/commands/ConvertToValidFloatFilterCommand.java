package em.libs.jfxtableview.filterFields.commands;

public class ConvertToValidFloatFilterCommand implements ConvertToValidFilterCommand {

    private String originalText;

    public ConvertToValidFloatFilterCommand(String originalText) {
        this.originalText = originalText;
    }

    @Override
    public String execute() {
        return getValidStringForFilterText(originalText);
    }

    private String getValidStringForFilterText(String text) {
        StringBuilder resultText = new StringBuilder();
        boolean isDot = false;
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '.' && !isDot && resultText.length() > 0) {
                resultText.append(chars[i]);
                isDot = true;
            }

            if (Character.isDigit(chars[i]) || (resultText.length() == 0 && chars[i] == '-')) {
                resultText.append(chars[i]);
            }
        }
        return resultText.toString();
    }
}
