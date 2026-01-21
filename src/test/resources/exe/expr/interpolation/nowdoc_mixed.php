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
heredoc after nowdoc
===================
Now variables work: $var
And expressions: $num
HEREDOC;
?>