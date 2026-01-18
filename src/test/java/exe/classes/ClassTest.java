package exe.classes;

import exe.utils.ExternalProcessDDT;
import exe.utils.ddt.ErrorCase;
import exe.utils.ddt.SuccessCase;

import java.util.stream.Stream;

class ClassTest extends ExternalProcessDDT {

    @Override
    protected String getName() {
        return "classes";
    }

    @Override
    protected Stream<SuccessCase> fileCases() {
        return Stream.of(
                // Базовые тесты классов
                new SuccessCase("simple_class.php",
                        "Hello from SimpleClass!"),

                // Конструкторы и деструкторы
                new SuccessCase("constructor_destructor.php",
                        "Object created: First\n" +
                                "Object created: Second\n" +
                                "Total objects: 2\n" +
                                "Name: First\n" +
                                "Object destroyed: First\n" +
                                "Object destroyed: Second\n"),

                // Наследование
                new SuccessCase("inheritance.php",
                        "Parent method called\n" +
                                "Parent value\n" +
                                "From Child (overridden)\n" +
                                "From Parent\n"),

                // Статические члены
                new SuccessCase("static_members.php",
                        "PI: 3.14159\n" +
                                "Area of circle (r=2): 12.56636\n" +
                                "Counter: 1\n" +
                                "Counter: 2\n" +
                                "Final Counter: 2\n"),

                // Модификаторы доступа
                new SuccessCase("visibility.php",
                        "Public\n" +
                                "Public|Protected|Private\n" +
                                "Protected\n" +
                                "Cannot access private\n"),

                // Абстрактные классы и интерфейсы
                new SuccessCase("abstract_interface.php",
                        "Rex: Woof! Woof!\n" +
                                "Whiskers: Meow!\n" +
                                "Rex is eating bone\n" +
                                "Rex is running\n"),

                // Магические методы
                new SuccessCase("magic_methods.php",
                        "Test\n" +
                                "123\n" +
                                "MagicDemo object with data: Test, 123\n" +
                                "Calling undefined method 'undefinedMethod' with arguments: arg1, arg2\n" +
                                "Calling undefined static method 'undefinedStaticMethod' with arguments: static_arg\n" +
                                "Property 'name' exists\n"),

                // Трейты
                new SuccessCase("trait_test.php",
                        "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}: Application started\n" +
                                "[HELLO WORLD]\n" +
                                "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}: Application finished\n")
        );
    }

    @Override
    protected Stream<ErrorCase> fileErrorCases() {
        return Stream.of(
                // Ошибки с классами
                new ErrorCase("class_error.php"),
                new ErrorCase("visibility_error.php"),
                new ErrorCase("abstract_error.php"),
                new ErrorCase("final_error.php"),
                new ErrorCase("type_error.php"),
                new ErrorCase("constructor_error.php")
        );
    }
}