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
                new SuccessCase("<?php $obj = new stdClass(); $obj->name = 'Alice'; echo \"Name: $obj->name\";", "Name: Alice"),

                // Интерполяция с доступом к элементам массива
                new SuccessCase("<?php $arr = [0 => 'zero', 1 => 'one']; echo \"First: $arr[0]\";", "First: zero"),
                new SuccessCase("<?php $arr = ['key' => 'value']; echo \"Value: $arr[key]\";", "Value: value"),
                new SuccessCase("<?php $index = 1; $arr = [0 => 'a', 1 => 'b']; echo \"Element: $arr[$index]\";", "Element: b"),

                // Сложная интерполяция в фигурных скобках
                new SuccessCase("<?php $a = 5; echo \"Value: {$a}\";", "Value: 5"),
                new SuccessCase("<?php $obj = new stdClass(); $obj->prop = 'test'; echo \"Prop: {$obj->prop}\";", "Prop: test"),
                new SuccessCase("<?php $arr = [0 => 'first']; echo \"Element: {$arr[0]}\";", "Element: first"),

                // Сложные выражения в интерполяции
                new SuccessCase("<?php $a = 5; $b = 3; echo \"Sum: {$a + $b}\";", "Sum: 8"),
                new SuccessCase("<?php $str = 'hello'; echo \"Length: {$str . ' world'}\";", "Length: hello world"),
                new SuccessCase("<?php $a = 10; echo \"Result: {($a > 5 ? 'big' : 'small')}\";", "Result: big"),

                // Статические свойства и методы в интерполяции
                new SuccessCase("<?php class Calc { public static $pi = 3.14; } echo \"PI: {Calc::$pi}\";", "PI: 3.14"),
                new SuccessCase("<?php class Math { public static function square($n) { return $n * $n; } } $x = 4; echo \"Square: {Math::square($x)}\";", "Square: 16"),

                // Вызовы методов в интерполяции
                new SuccessCase("<?php class Greeter { public function hello() { return 'Hi'; } } $g = new Greeter(); echo \"Say: {$g->hello()}\";", "Say: Hi"),
                new SuccessCase("<?php $arr = [1, 2, 3]; echo \"Count: {$arr[1]}\";", "Count: 2"),

                // Вложенные доступы
                new SuccessCase("<?php $obj = new stdClass(); $obj->arr = ['key' => 'val']; echo \"Nested: {$obj->arr['key']}\";", "Nested: val"),
                new SuccessCase("<?php class Container { public $child; } $c = new Container(); $c->child = new stdClass(); $c->child->value = 42; echo \"Deep: {$c->child->value}\";", "Deep: 42"),

                // HEREDOC с интерполяцией
                new SuccessCase("<?php $name = 'World'; echo <<<EOT\nHello $name\nEOT;", "Hello World"),
                new SuccessCase("<?php $a = 5; $b = 3; echo <<<END\nResult: {$a + $b}\nEND;", "Result: 8"),
                new SuccessCase("<?php $var = 'test'; echo <<<TAG\nValue: $var\nMore: {$var . 'ing'}\nTAG;", "Value: test\nMore: testing"),

                // NOWDOC без интерполяции
                new SuccessCase("<?php echo <<<'NOW'\nNo interpolation here\nNOW;", "No interpolation here"),
                new SuccessCase("<?php $var = 'ignored'; echo <<<'TEXT'\n$var is not interpolated\nTEXT;", "$var is not interpolated"),

                // HEREDOC со сложными выражениями
                new SuccessCase("<?php class Test { public $x = 10; } $t = new Test(); echo <<<DOC\nValue: {$t->x}\nArray: {[1,2,3][1]}\nDOC;", "Value: 10\nArray: 2"),

                // Смешанная интерполяция
                new SuccessCase("<?php $x = 1; $y = 2; echo \"Simple: $x, Complex: {$x + $y}, Direct: \" . ($x * $y);", "Simple: 1, Complex: 3, Direct: 2"),

                // Интерполяция с экранированием
                new SuccessCase("<?php $var = 'value'; echo \"\\$var = $var\";", "$var = value"),
                new SuccessCase("<?php echo \"Text with \\\"quotes\\\" and $dollar\";", "Text with \"quotes\" and $dollar"),

                // Многострочная интерполяция
                new SuccessCase("<?php $name = 'John'; $age = 25; echo <<<INFO\nName: $name\nAge: $age\nTotal: {$name . ' ' . $age}\nINFO;", "Name: John\nAge: 25\nTotal: John 25"),

                // Специальные символы в HEREDOC/NOWDOC
                new SuccessCase("<?php echo <<<HTML\n<div class=\"test\">Content</div>\nHTML;", "<div class=\"test\">Content</div>"),
                new SuccessCase("<?php echo <<<'JSON'\n{\"key\": \"value\"}\nJSON;", "{\"key\": \"value\"}"),

                // Интерполяция с инкрементом
                new SuccessCase("<?php $i = 0; echo \"Count: {$i++}, now: {$i}\";", "Count: 0, now: 1"),
                new SuccessCase("<?php $i = 5; echo \"Pre: {--$i}, Post: {$i--}\";", "Pre: 4, Post: 4"),

                // Булевая интерполяция
                new SuccessCase("<?php $bool = true; echo \"Bool: {$bool}\";", "Bool: 1"),
                new SuccessCase("<?php $bool = false; echo \"Bool: {$bool}\";", "Bool: "),

                // Null в интерполяции
                new SuccessCase("<?php $null = null; echo \"Null: {$null}\";", "Null: "),

                // Интерполяция с приведением типов
                new SuccessCase("<?php $num = '123'; echo \"Number: {(int)$num + 1}\";", "Number: 124"),
                new SuccessCase("<?php $arr = [1,2,3]; echo \"First: {(string)$arr[0]}\";", "First: 1"),

                // Динамические имена свойств
                new SuccessCase("<?php $obj = new stdClass(); $prop = 'value'; $obj->$prop = 'test'; echo \"Dynamic: {$obj->$prop}\";", "Dynamic: test"),
                new SuccessCase("<?php $obj = new stdClass(); $obj->field = 'data'; $name = 'field'; echo \"Indirect: {$obj->$name}\";", "Indirect: data"),

                // Конкатенация внутри интерполяции
                new SuccessCase("<?php $a = 'Hello'; $b = 'World'; echo \"Message: {$a . ' ' . $b}\";", "Message: Hello World"),
                new SuccessCase("<?php $parts = ['Hello', 'PHP']; echo \"Joined: {implode(', ', $parts)}\";", "Joined: Hello, PHP"),

                // Пустые строки и интерполяция
                new SuccessCase("<?php echo <<<EOT\n\n\nEOT;", "\n\n"),

                // Интерполяция с нулевым индексом
                new SuccessCase("<?php $arr = [0 => 'zero']; echo \"{$arr[0]}\";", "zero"),

                // Интерполяция булевых значений
                new SuccessCase("<?php $true = true; $false = false; echo \"True: {$true}, False: {$false}\";", "True: 1, False: "),

                // Интерполяция чисел с плавающей точкой
                new SuccessCase("<?php $pi = 3.14159; echo \"PI: {$pi}\";", "PI: 3.14159"),

                // Большие heredoc
                new SuccessCase("""
                        <?php $name = 'Test'; echo <<<LONG
                        Line 1 with $name
                        Line 2 with {$name . '2'}
                        Line 3
                        Line 4
                        LONG;""",
                        "Line 1 with Test\nLine 2 with Test2\nLine 3\nLine 4\n"),

                // Nowdoc со специальными символами
                new SuccessCase("""
                        <?php echo <<<'SPECIAL'
                        Line with \\t tab
                        Line with \\n newline (not actual)
                        Line with $dollar
                        Line with {braces}
                        SPECIAL;""",
                        "Line with \\t tab\nLine with \\n newline (not actual)\nLine with $dollar\nLine with {braces}\n"),

                // Интерполяция с type casting
                new SuccessCase("<?php $num = '123'; echo \"Int: {((int)$num) * 2}\";", "Int: 246"),

                // Интерполяция в аргументах функции
                new SuccessCase("<?php function test($str) { return $str; } $var = 'hello'; echo test(\"Message: $var\");", "Message: hello")

        );
    }

    @Override
    protected Stream<ErrorCase> phpErrorCases() {
        return Stream.of(
                // Ошибки интерполяции
                new ErrorCase("<?php echo 324 + 'hi';"),
                new ErrorCase("<?php echo \"Test $undefinedVar\";"),
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
                new ErrorCase("<?php echo \"Test {self::undefined}\";"),
                new ErrorCase("<?php echo \"Test {$array[invalid key]}\";"),

                // Синтаксические ошибки интерполяции
                new ErrorCase("<?php echo \"Test {{$var}\";"),
                new ErrorCase("<?php echo \"Test {$var}}\";"),
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

                // Файл с nowdoc и смешанным содержимым
                new SuccessCase("nowdoc_mixed.php",
                        """
                                Before nowdoc: VARIABLE
                                
                                NOWDOC CONTENT
                                ==============
                                This is a template with $var not interpolated.
                                Also {1 + 2} is not calculated.
                                The number is $num.
                                
                                But we can close and open new heredoc:
                                
                                HEREDOC AFTER NOWDOC
                                =====================
                                Now variables work: VARIABLE
                                And expressions: 84
                                """),

                // Файл с вложенными структурами и интерполяцией
                new SuccessCase("nested_interpolation.php",
                        """
                                DEPARTMENT REPORT
                                =================
                                Department: IT, Employees: 2
                                
                                Employee details:
                                1. Name: John, Salary: $50000
                                2. Name: Jane, Salary: $55000
                                
                                Total salary: $105000
                                """),

                new SuccessCase("heredoc_multiline.php",
                        """
                                PRODUCT CATALOG
                                ===============
                                
                                PRODUCT LIST:
                                -------------
                                1. Laptop
                                    Category: Electronics
                                    Price: $1299.99
                                
                                2. Coffee Mug
                                    Category: Kitchen
                                    Price: $12.50
                                
                                3. Book
                                    Category: Education
                                    Price: $24.99
                                
                                SUMMARY:
                                --------
                                Total products: 3
                                Total value: $1337.48
                                
                                Generated: \\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}
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
                                ; Generated on: \\d{4}-\\d{2}-\\d{2}
                                
                                [application]
                                title = "Configuration File"
                                version = "1.0.0"
                                
                                [settings]
                                debug = 1
                                cache = 3600
                                db_host = "localhost"
                                
                                """),

                new SuccessCase("interpolation_edge_cases.php",
                        """
                                Test 1 (chained): value
                                Test 2 (method call): value
                                Test 3 (static): static
                                Test 4 (array with variable index): first
                                Test 5 (nested array): c
                                Test 6 (ternary): zero
                                Test 7 (increment): 1
                                Test 8 (function call): HELLO
                                Test 9 (variable variable): dynamic value
                                <div class="container">
                                    <h1>value</h1>
                                    <p>Price: $first</p>
                                    <p>Escaped: \\$not_a_variable</p>
                                </div>
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
