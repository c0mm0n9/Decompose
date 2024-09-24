import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.lang.reflect.Modifier;

public class Decompose {
    public static void main(String[] args){
        Class<?> clazz = Decompose.class;
        Method[] methods = clazz.getDeclaredMethods();
        for(Method method : methods){
            System.out.println("Method Name: " + method.getName());

            System.out.println("Return Type: " + method.getReturnType().getName());

            Class<?>[] parameterTypes = method.getParameterTypes();
            System.out.print("Parameter Types: ");
            for (Class<?> paramType : parameterTypes) {
                System.out.print(paramType.getName() + " ");
            }
            System.out.println();

            System.out.println("Modifiers: " + Modifier.toString(method.getModifiers()));

            System.out.println("---------------------------");
        }
    }
    public static <T> List<T> merge(List<T> ... args){
        return new ArrayList<>(new LinkedHashSet<>(Arrays.stream(args).flatMap(List::stream).collect(Collectors.toList())));
    }

}
