import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

public class BadIntrospectAlternative {

    private static String[] section(String s, String separator) {
        int index = s.indexOf(separator);
        if (index < 0) {
            return new String[] { s, "" };
        }
        return new String[] { s.substring(0, index), s.substring(index + separator.length()) };
    }

    private static Signature getSignature(String methodString) {
        String title = "";
        String description = "";
        List<Parameter> parameters = new ArrayList<>();
        List<String> comments = new ArrayList<>();

        String regex = "(\\w+)\\s+(\\w+)\\(([^)]*)\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(methodString);

        if (matcher.find()) {
            title = matcher.group(2);
            String paramString = matcher.group(3).trim();

            String[] params = paramString.split(",");

            String paramRegex = "(\\w+\\s+\\w+)(?:\\s*//\\s*(.*))?";

            for (String param : params) {
                Matcher paramMatcher = Pattern.compile(paramRegex).matcher(param.trim());
                if (paramMatcher.find()) {
                    String fullParam = paramMatcher.group(1);
                    String comment = paramMatcher.group(2) != null ? paramMatcher.group(2).trim() : "";

                    String[] paramParts = section(fullParam, " ");
                    parameters.add(new Parameter(paramParts[0],paramParts[1],comment));
                }
            }
        }

        String[] lines = methodString.split("\n");
        if(lines.length>0) description = lines[0].trim().substring(2).trim();

        for (int i = 1; i<lines.length; i++) {
            String line = lines[i];
            if (line.trim().startsWith("//")) {
                String comment = line.trim().substring(2).trim();
                comments.add(comment);
            }
        }

        return new Signature(title,description,parameters,comments);
    }


    public static void main(String[] args) {
        String methodString =
                "public void exampleMethod(int param1 //comment, String param2) {\n" +
                "    // This is a sample method\n" +
                "    // Example usage\n" +
                "}\n";

        Signature signature = getSignature(methodString);
        System.out.println(signature.toString());
    }

    private static class Signature{
        String title;
        String description;
        List<Parameter> parameters;
        List<String> comments;
        public Signature(String title,String description, List<Parameter> parameters, List<String> comments){
            this.title = title;
            this.description = description;
            this.parameters = parameters;
            this.comments = comments;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Title: ").append(title).append("\n")
                    .append("Description: ").append(description).append("\n")
                    .append("Parameters: ").append(parameters.stream().map(Parameter::toString).collect(Collectors.toList())).append("\n")
                    .append("Comments: ").append(comments.toString());
            return sb.toString();
        }

    }
    private static class Parameter{
        String type;
        String name;
        String comment;
        public Parameter(String type,String name,String comment){
            this.type=type;
            this.name=name;
            this.comment=comment;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Parameter{")
                    .append("type='").append(type).append('\'')
                    .append(", name='").append(name).append('\'')
                    .append(", comment='").append(comment).append('\'')
                    .append('}');
            return sb.toString();
        }
    }
}
