package exe.expr;

import exe.utils.ExternalProcessDDT;
import exe.utils.ddt.ErrorCase;
import exe.utils.ddt.SuccessCase;

import java.util.stream.Stream;

class InterpolationTest extends ExternalProcessDDT {

    @Override
    protected Stream<SuccessCase> phpCases() {
        return Stream.of(
                // Простые строки
                new SuccessCase("<?php echo 'hi';", "hi"),
                new SuccessCase("<?php echo '123';", "123"),
                new SuccessCase("<?php echo 2 + 3;", "5"),
                new SuccessCase("<?php echo \"i<\" . 3 . 'u';", "i<3u"),

                // Простая интерполяция переменных
                new SuccessCase("<?php $name = 'John'; echo \"Hello $name\";", "Hello John"),
                new SuccessCase("<?php $a = 5; $b = 10; echo \"Sum: $a + $b = \" . ($a + $b);", "Sum: 5 + 10 = 15"),
                new SuccessCase("<?php $var = 'test'; echo \"Value: $var\";", "Value: test"),

                // Интерполяция с доступом к свойствам
                new SuccessCase("<?php class Test { public $prop = 'value'; } $obj = new Test(); echo \"Prop: $obj->prop\";", "Prop: value"),

                // Интерполяция с доступом к элементам массива
                new SuccessCase("<?php $arr = [0 => 'zero', 1 => 'one']; echo \"First: $arr[0]\";", "First: zero"),
                new SuccessCase("<?php $arr = ['key' => 'value']; echo \"Value: $arr[key]\";", "Value: value"),
                new SuccessCase("<?php $index = 1; $arr = [0 => 'a', 1 => 'b']; echo \"Element: $arr[$index]\";", "Element: b"),

                // Сложная интерполяция в фигурных скобках
                new SuccessCase("<?php $a = 5; echo \"Value: {$a}\";", "Value: 5"),
                new SuccessCase("<?php $arr = [0 => 'first']; echo \"Element: {$arr[0]}\";", "Element: first"),

                // Вызовы методов в интерполяции
                new SuccessCase("<?php class Greeter { public function hello() { return 'Hi'; } } $g = new Greeter(); echo \"Say: {$g->hello()}\";", "Say: Hi"),
                new SuccessCase("<?php $arr = [1, 2, 3]; echo \"Count: {$arr[1]}\";", "Count: 2"),

                // HEREDOC с интерполяцией
                new SuccessCase("<?php $name = 'World'; echo <<<EOT\nHello $name\nEOT;", "Hello World"),

                // NOWDOC без интерполяции
                new SuccessCase("<?php echo <<<'NOW'\nNo interpolation here\nNOW;", "No interpolation here"),
                new SuccessCase("<?php $var = 'ignored'; echo <<<'TEXT'\n$var is not interpolated\nTEXT;", "$var is not interpolated"),

                // Интерполяция с экранированием
                new SuccessCase("<?php $var = 'value'; echo \"\\$var = $var\";", "$var = value"),
                new SuccessCase("<?php echo \"Text with \\\"quotes\\\" \";", "Text with \"quotes\" "),

                // Специальные символы в HEREDOC/NOWDOC
                new SuccessCase("<?php echo <<<HTML\n<div class=\"test\">Content</div>\nHTML;", "<div class=\"test\">Content</div>"),
                new SuccessCase("<?php echo <<<'JSON'\n{\"key\": \"value\"}\nJSON;", "{\"key\": \"value\"}"),

                // Булевая интерполяция
                new SuccessCase("<?php $bool = true; echo \"Bool: {$bool}\";", "Bool: 1"),
                new SuccessCase("<?php $bool = false; echo \"Bool: {$bool}\";", "Bool: "),

                // Null в интерполяции
                new SuccessCase("<?php $null = null; echo \"Null: {$null}\";", "Null: "),

                // Пустые строки и интерполяция
                new SuccessCase("<?php echo <<<EOT\n\n\nEOT;", "\n\n"),

                // Интерполяция с нулевым индексом
                new SuccessCase("<?php $arr = [0 => 'zero']; echo \"{$arr[0]}\";", "zero"),

                // Интерполяция булевых значений
                new SuccessCase("<?php $true = true; $false = false; echo \"True: {$true}, False: {$false}\";", "True: 1, False: "),

                // Интерполяция чисел с плавающей точкой
                new SuccessCase("<?php $pi = 3.141590118408203; echo \"PI: {$pi}\";", "PI: 3.141590118408203"),

                // Большие heredoc
                new SuccessCase("""
                        <?php $name = 'Test'; echo <<<LONG
                        Line 1 with $name
                        Line 2
                        Line 3
                        Line 4
                        LONG;""",
                        "Line 1 with Test\nLine 2\nLine 3\nLine 4\n"),

                // Nowdoc со специальными символами
                new SuccessCase("""
                        <?php echo <<<'SPECIAL'
                        Line with \\t tab
                        Line with \\n newline (not actual)
                        Line with $dollar
                        Line with {braces}
                        SPECIAL;""",
                        "Line with \\t tab\nLine with \\n newline (not actual)\nLine with $dollar\nLine with {braces}\n"),

                // Интерполяция в аргументах функции
                new SuccessCase("<?php function test($str) { return $str; } $var = 'hello'; echo test(\"Message: $var\");", "Message: hello")

        );
    }

    @Override
    protected Stream<ErrorCase> phpErrorCases() {
        return Stream.of(
                // Ошибки интерполяции
                new ErrorCase("<?php echo 324 + 'hi';"),
                new ErrorCase("<?php echo \"Test {$undefined->prop}\";"),
                new ErrorCase("<?php echo \"Test $arr[undefined]\";"),
                new ErrorCase("<?php echo \"Test {$obj->undefinedMethod()}\";"),
                new ErrorCase("<?php echo \"Test {$1 + 2}\";"),
                new ErrorCase("<?php echo \"Test {$var[}\";"),
                new ErrorCase("<?php echo \"Test {$var[1}\";"),
                new ErrorCase("<?php echo \"Test {$var->}\";"),
                new ErrorCase("<?php echo \"Test {$var[0][1}\";"),

                // Ошибки в HEREDOC/NOWDOC
                new ErrorCase("<?php echo <<<END\nNo closing marker"),
                new ErrorCase("<?php echo <<<END\nContent\nEND\nMore text after END;"),
                new ErrorCase("<?php echo <<<END\nEND\n; // Пустой heredoc с лишней точкой с запятой"),
                new ErrorCase("<?php echo <<<'END'\nContent\nEND\nExtra"),

                // Ошибки доступа в интерполяции
                new ErrorCase("<?php echo \"Test {$private->property}\";"),
                new ErrorCase("<?php echo \"Test {$array[invalid key]}\";"),

                // Синтаксические ошибки интерполяции
                new ErrorCase("<?php echo \"Test {$var[]['key']}\";"),
                new ErrorCase("<?php echo \"Test {$obj->}\";"),
                new ErrorCase("<?php echo \"Test {$obj->123}\";"),

                // Ошибки в сложных выражениях
                new ErrorCase("<?php echo \"Test {$a + }\";"),
                new ErrorCase("<?php echo \"Test {$var[0]->}\";"),
                new ErrorCase("<?php echo \"Test {$$var}\";"), // если не поддерживается
                new ErrorCase("<?php echo \"Test {${var}}\";"),

                // Ошибки с nowdoc
                new ErrorCase("<?php echo <<<'NOW'\nContent without closing"),
                new ErrorCase("<?php echo <<<'END'\nContent\nend;"), // регистр имеет значение
                new ErrorCase("<?php echo <<<'END'\nContent\nEND\nEND;") // дублирование

        );
    }

    @Override
    protected String getName() {
        return "expr/interpolation";
    }

    @Override
    protected Stream<SuccessCase> fileCases() {
        return Stream.of(
                // Файл с многострочным heredoc и сложной интерполяцией
                new SuccessCase("heredoc_complex.php",
                        """
                                USER REPORT
                                ===========
                                Name: Alice
                                Age: 30
                                Info: Alice (30)
                                Favorite items: apple, banana, cherry
                                Total items: 3
                                """),

                // Файл с вложенными структурами и интерполяцией
                new SuccessCase("nested_interpolation.php",
                        """
                                DEPARTMENT REPORT
                                =================
                                
                                Employee details:
                                1. Name: John, Salary: $50000
                                2. Name: Jane, Salary: $55000
                                """),


                new SuccessCase("nowdoc_template.php",
                        """
                                ; <?php /* DO NOT EDIT THIS FILE DIRECTLY */ ?>
                                ; Auto-generated configuration file
                                
                                [application]
                                title = "$data['title']"
                                version = "1.0.0"
                                
                                [settings]
                                debug = $data['settings']['debug']
                                cache = $data['settings']['cache']
                                db_host = "$data['settings']['db_host']"
                                
                                ; This is a comment
                                ; All $variables are treated as literal text
                                
                                ---
                                ; Dynamic configuration file
                                
                                [application]
                                title = "Configuration File"
                                version = "1.0.0"
                                
                                [settings]
                                debug = 1
                                cache = 3600
                                db_host = "localhost"
                                
                                """)
        );
    }

    @Override
    protected Stream<ErrorCase> fileErrorCases() {
        return Stream.of(
                // Файл с ошибками интерполяции в heredoc
                new ErrorCase("heredoc_errors.php"),

                // Файл с неправильным синтаксисом nowdoc
                new ErrorCase("nowdoc_errors.php"),

                // Файл с вложенными ошибками доступа
                new ErrorCase("nested_errors.php")
        );
    }

}
