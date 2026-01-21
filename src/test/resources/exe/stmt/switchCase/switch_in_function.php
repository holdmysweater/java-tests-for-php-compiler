<?php
function getGrade($score)
{
    switch (true) {
        case ($score >= 90):
            return "Grade: A";
        case ($score >= 80):
            return "Grade: B";
        case ($score >= 70):
            return "Grade: C";
        default:
            return "Grade: F";
    }
}

echo getGrade(95) . "\n";
echo getGrade(85) . "\n";
echo getGrade(75) . "\n";
echo getGrade(65) . "\n";
?>