<?php
$var = 'VARIABLE';
$num = 42;

echo 'Before nowdoc: ' . $var . "\n\n";

echo <<<'TEMPLATE'
NOWDOC CONTENT
==============
This is a template with $var not interpolated.
Also {1 + 2} is not calculated.
The number is $num.

But we can close and open new heredoc:
TEMPLATE;

echo <<<HEREDOC

HEREDOC AFTER NOWDOC
=====================
Now variables work: $var
And expressions: {$num * 2}
HEREDOC;
?>