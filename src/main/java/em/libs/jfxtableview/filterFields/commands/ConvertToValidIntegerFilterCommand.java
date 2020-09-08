package em.libs.jfxtableview.filterFields.commands;

public class ConvertToValidIntegerFilterCommand implements ConvertToValidFilterCommand {

    private String originalText;

    public ConvertToValidIntegerFilterCommand(String originalText) {
        this.originalText = originalText;
    }

    @Override
    public String execute() {
        return getValidStringForFilterText(originalText);
    }

    private String getValidStringForFilterText(String text) {
        StringBuilder resultText = new StringBuilder();
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (Character.isDigit(chars[i]) || (resultText.length() == 0 && chars[i] == '-')) {
                resultText.append(chars[i]);
            }
        }
        return resultText.toString();
    }
}
