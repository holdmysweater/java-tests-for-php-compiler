package exe.stmt;

import exe.utils.ExternalProcessDDT;
import exe.utils.ddt.ErrorCase;
import exe.utils.ddt.SuccessCase;

import java.util.stream.Stream;

class ForeachTest extends ExternalProcessDDT {

    @Override
    protected String getName() {
        return "stmt/foreach";
    }

    @Override
    protected Stream<SuccessCase> fileCases() {
        return Stream.of(
                // Простой foreach только со значением
                new SuccessCase("foreach_simple.php",
                        "apple\n" +
                                "banana\n" +
                                "cherry\n"),

                // Foreach с ключом и значением
                new SuccessCase("foreach_key_value.php",
                        "0: apple\n" +
                                "1: banana\n" +
                                "2: cherry\n"),

                // Альтернативный синтаксис только со значением
                new SuccessCase("foreach_alternative_syntax.php",
                        "apple\n" +
                                "banana\n" +
                                "cherry\n"),

                // Альтернативный синтаксис с ключом и значением
                new SuccessCase("foreach_key_value_alternative.php",
                        "0: apple\n" +
                                "1: banana\n" +
                                "2: cherry\n"),

                // Foreach с ассоциативным массивом
                new SuccessCase("foreach_associative.php",
                        "a: apple\n" +
                                "b: banana\n" +
                                "c: cherry\n"),
                // Вложенные foreach
                new SuccessCase("foreach_nested.php",
                        "Outer: red\n" +
                                "  Inner: apple\n" +
                                "  Inner: strawberry\n" +
                                "Outer: yellow\n" +
                                "  Inner: banana\n" +
                                "  Inner: lemon\n"),

                // Foreach с break
                new SuccessCase("foreach_with_break.php",
                        "apple\n" +
                                "banana\n"),

                // Foreach с continue
                new SuccessCase("foreach_with_continue.php",
                        "apple\n" +
                                "cherry\n"),

                // Foreach с пустым массивом
                new SuccessCase("foreach_empty_array.php",
                        "Empty array\n"),

                // Foreach с массивом массивов
                new SuccessCase("foreach_multidimensional.php",
                        "Fruit: apple, Color: red\n" +
                                "Fruit: banana, Color: yellow\n"),

                // Foreach с ключами-строками
                new SuccessCase("foreach_string_keys.php",
                        "first: 1\n" +
                                "second: 2\n" +
                                "third: 3\n")
        );
    }

    @Override
    protected Stream<ErrorCase> fileErrorCases() {
        return Stream.of(
                // Ошибки синтаксиса
                new ErrorCase("error_foreach_missing_as.php"),
                new ErrorCase("error_foreach_missing_dollar.php"),
                new ErrorCase("error_foreach_missing_endforeach.php"),
                new ErrorCase("error_foreach_invalid_expression.php"),
                new ErrorCase("error_foreach_missing_paren.php"),
                new ErrorCase("error_foreach_missing_colon.php"),
                new ErrorCase("error_foreach_extra_arrow.php")
        );
    }
}