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
                                "Name: First\n" +
                                "Object destroyed: Second\n" +
                                "Object destroyed: First\n"),

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
                        "PI: 3.141590118408203\n" +
                                "Area of circle (r=2): 12.566360473632812\n" +
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

                // Константы класса
                new SuccessCase("class_with_constants.php",
                        "3.141590118408203\n" +
                                "3.141590118408203, 2.718280076980591, 42\n"),

                // Несколько констант
                new SuccessCase("class_multiple_constants.php",
                        "1.0.0\n" +
                                "Version: 1.0.0, Max: 1024\n"),

                // Типизированные свойства
                new SuccessCase("class_with_typed_properties.php",
                        "John is 25 years old\n"),

                // Типизированные свойства со значениями по умолчанию
                new SuccessCase("class_typed_properties_default.php",
                        "Book: 59.96999931335449\n"),

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
                new ErrorCase("final_error.php"),
                new ErrorCase("type_error.php"),
                new ErrorCase("constructor_error.php"),
                new ErrorCase("error_class_duplicate_property.php"),
                new ErrorCase("error_missing_implementation.php"),
                new ErrorCase("error_protected_access.php"),
                new ErrorCase("error_private_access.php"),
                new ErrorCase("error_static_nonstatic.php")
        );
    }
}