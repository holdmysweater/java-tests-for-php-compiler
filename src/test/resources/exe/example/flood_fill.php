<?php
// Read number of rows (n) and columns (m), each on its own line
$n = trim(fgets(STDIN));
$m = trim(fgets(STDIN));

// Read the matrix as a 2D array of characters
$matrix = [];
for ($i = 0; $i < $n; $i++) {
    // Read one row like "00110"
    $row = trim(fgets(STDIN));

    // Convert the row string into an array of characters
    $matrix[$i] = [];
    for ($j = 0; $j < $m; $j++) {
        $matrix[$i][$j] = $row[$j];
    }
}

// Read start position (x, y) and the new fill value, each on its own line
$x = trim(fgets(STDIN));        // start row index (0-based)
$y = trim(fgets(STDIN));        // start column index (0-based)
$newColor = trim(fgets(STDIN)); // character to fill with (e.g., "2")

/**
 * Recursive flood fill:
 * Replaces all connected cells (4-directionally) that match $oldColor with $newColor.
 */
function floodFill(&$matrix, $x, $y, $oldColor, $newColor, $n, $m)
{
    // 1) Stop if out of bounds
    if ($x < 0 || $x >= $n || $y < 0 || $y >= $m) {
        return;
    }

    // 2) Stop if this cell is NOT the target color
    if ($matrix[$x][$y] !== $oldColor) {
        return;
    }

    // 3) Fill current cell
    $matrix[$x][$y] = $newColor;

    // 4) Recurse to 4 neighbors
    floodFill($matrix, $x + 1, $y, $oldColor, $newColor, $n, $m); // down
    floodFill($matrix, $x - 1, $y, $oldColor, $newColor, $n, $m); // up
    floodFill($matrix, $x, $y + 1, $oldColor, $newColor, $n, $m); // right
    floodFill($matrix, $x, $y - 1, $oldColor, $newColor, $n, $m); // left
}

// Remember the color at the starting cell
$oldColor = $matrix[$x][$y];

// Only run fill if oldColor differs from newColor
if ($oldColor !== $newColor) {
    floodFill($matrix, $x, $y, $oldColor, $newColor, $n, $m);
}

// Output final matrix
for ($i = 0; $i < $n; $i++) {
    echo implode('', $matrix[$i]) . "\n";
}
?>