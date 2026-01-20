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

                // Полное наследование
                new SuccessCase("class_inheritance_full.php",
                        "Woof! Woof!\n" +
                                "Rex is 3 years old, Breed: Golden Retriever\n"),

                // Статические члены
                new SuccessCase("static_members.php",
                        "PI: 3.14159\n" +
                                "Area of circle (r=2): 12.56636\n" +
                                "Counter: 1\n" +
                                "Counter: 2\n" +
                                "Final Counter: 2\n"),

                // Статические свойства
                new SuccessCase("class_static_properties.php",
                        "Count: 3\n" +
                                "Static method: 3\n"),

                // Модификаторы доступа
                new SuccessCase("visibility.php",
                        "Public\n" +
                                "Public|Protected|Private\n" +
                                "Protected\n" +
                                "Cannot access private\n"),

                // Модификаторы свойств
                new SuccessCase("class_property_modifiers.php",
                        "Public\n" +
                                "Static Public\n" +
                                "Public|Protected|Private|Static Public|Static Protected|Static Private\n"),

                // Модификаторы методов
                new SuccessCase("class_method_modifiers.php",
                        "Public Method\n" +
                                "Static Public Method\n" +
                                "Public Method|Protected Method|Private Method|Static Public Method|Static Protected Method|Static Private Method\n"),

                // Абстрактные классы и интерфейсы
                new SuccessCase("abstract_interface.php",
                        "Rex: Woof! Woof!\n" +
                                "Whiskers: Meow!\n" +
                                "Rex is eating bone\n" +
                                "Rex is running\n"),

                // Простой интерфейс
                new SuccessCase("class_interface_simple.php",
                        "Logging to file: Test message\n"),

                // Интерфейс с несколькими методами
                new SuccessCase("interface_multiple_methods.php",
                        "Drawing circle with radius: 5.0\n" +
                                "Area: 78.53975\n" +
                                "Scaled to radius: 7.5\n"),

                // Интерфейс с константами
                new SuccessCase("interface_with_constants.php",
                        "OK: 200\n" +
                                "Response: 200\n"),

                // Абстрактные методы
                new SuccessCase("class_abstract_methods.php",
                        "Color: red\n" +
                                "Area: 25.0\n"),

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
                                "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}: Application finished\n"),

                // Константы класса
                new SuccessCase("class_with_constants.php",
                        "3.14159\n" +
                                "3.14159, 2.71828, 42\n"),

                // Несколько констант
                new SuccessCase("class_multiple_constants.php",
                        "1.0.0\n" +
                                "Version: 1.0.0, Max: 1024\n"),

                // Типизированные свойства
                new SuccessCase("class_with_typed_properties.php",
                        "John is 25 years old\n"),

                // Типизированные свойства с значениями по умолчанию
                new SuccessCase("class_typed_properties_default.php",
                        "Book: 59.97\n"),

                // Final класс
                new SuccessCase("class_final.php",
                        "Cannot be extended\n" +
                                "Final method\n"),

                // Класс без свойств
                new SuccessCase("class_without_properties.php",
                        "Hello from empty class!\n"),

                // Сложная иерархия наследования
                new SuccessCase("class_complex_hierarchy.php",
                        "Method A\n" +
                                "Method B: A\n" +
                                "Method C: B, Method A\n")
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
                new ErrorCase("constructor_error.php"),
                new ErrorCase("error_class_duplicate_property.php"),
                new ErrorCase("error_interface_instantiation.php"),
                new ErrorCase("error_abstract_instantiation.php"),
                new ErrorCase("error_missing_implementation.php"),
                new ErrorCase("error_protected_access.php"),
                new ErrorCase("error_private_access.php"),
                new ErrorCase("error_static_nonstatic.php"),
                new ErrorCase("error_invalid_property_type.php"),
                new ErrorCase("error_class_redeclaration.php"),
                new ErrorCase("error_interface_redeclaration.php")
        );
    }
}