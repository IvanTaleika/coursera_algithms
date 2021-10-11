// helps to avoid char boxing
@FunctionalInterface
public interface CharUnaryOperator {

    char apply(char c);
}