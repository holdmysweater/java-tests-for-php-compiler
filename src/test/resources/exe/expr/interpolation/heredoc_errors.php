<?php
echo <<<END
Testing errors:
Undefined: {$undefined->property}
Syntax error: {$array[}
Math error: {1 / 0}
END;
?>