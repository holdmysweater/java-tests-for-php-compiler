package exe.example;

import exe.utils.ExternalProcessDDT;
import org.junit.jupiter.api.Test;

class BaseTest extends ExternalProcessDDT {

    @Override
    protected String getName() {
        return "example";
    }

    @Test
    void input() throws Exception {
        assertFileWithInput("input.php", "hello", "hello");
    }

    @Test
    void ifElseTest() throws Exception {
        assertFileWithInput("if_else.php", "12", "Greater than 10");
        assertFileWithInput("if_else.php", "8", "Greater than 5, but not greater than 10");
        assertFileWithInput("if_else.php", "3", "5 or less");
        assertFileWithInput("if_else.php", "1", "5 or less");
    }

    @Test
    void loopsTest() throws Exception {
        assertFileWithInput("loops.php", "3", "For loop: 1 2 3 \nWhile loop: 1 2 3 \nDo-while: 1 2 3 ");
        assertFileWithInput("loops.php", "1", "For loop: 1 \nWhile loop: 1 \nDo-while: 1 ");
        assertFileWithInput("loops.php", "0", "For loop: \nWhile loop: \nDo-while: 1 ");
    }

    @Test
    void functionParamsTest() throws Exception {
        assertFileWithInput("function_params.php", "5",
                "By value - original: 5, result: 10\n" +
                        "By reference - modified: 10\n" +
                        "Object - value: 10\n" +
                        "Array - first element: 5\n" +
                        "Array by reference - first element: 777");
    }

    @Test
    void bubbleSortTest() throws Exception {
        String input = "5\n3\n1\n4\n2\n5";
        String expected = "Original array: 3\n 1\n 4\n 2\n 5\n\n" +
                "Sorted array: 1\n 2\n 3\n 4\n 5";
        assertFileWithInput("bubble_sort.php", input, expected);
    }

    @Test
    void bubbleSortEmptyTest() throws Exception {
        String input = "0";
        String expected = "Original array: \n" +
                "Sorted array: ";
        assertFileWithInput("bubble_sort.php", input, expected);
    }

    @Test
    void bubbleSortSingleTest() throws Exception {
        String input = "1\n42";
        String expected = "Original array: 42\n\n" +
                "Sorted array: 42";
        assertFileWithInput("bubble_sort.php", input, expected);
    }

    @Test
    void floodFillTest() throws Exception {
        String input = "5\n" +
                "7\n" +
                "1111111\n" +
                "1000001\n" +
                "1011101\n" +
                "1000001\n" +
                "1111111\n" +
                "1\n" +
                "1\n" +
                "2";

        String expected = "1111111\n" +
                "1222221\n" +
                "1211121\n" +
                "1222221\n" +
                "1111111\n";

        assertFileWithInput("flood_fill.php", input, expected);
    }

    @Test
    void polymorphismDogTest() throws Exception {
        assertFileWithInput("polymorphism.php", "dog",
                "Name: Rex\n" +
                        "Sound: Woof!\n" +
                        "Additionally: Rex is wagging tail\n" +
                        "Through parent reference: Woof!");
    }

    @Test
    void polymorphismCatTest() throws Exception {
        assertFileWithInput("polymorphism.php", "cat",
                "Name: Whiskers\n" +
                        "Sound: Meow!\n" +
                        "Additionally: Whiskers is climbing a tree\n" +
                        "Through parent reference: Meow!");
    }

    @Test
    void polymorphismUnknownTest() throws Exception {
        assertFileWithInput("polymorphism.php", "bird",
                "Name: Unknown\n" +
                        "Sound: Some sound\n" +
                        "Through parent reference: Some sound");
    }

    @Test
    void superCallTest() throws Exception {
        assertFileWithInput("super_call.php", "10 5",
                "1. calculate() with super: 25\n" +
                        "2. getFullInfo() with super: Parent: 10\n" + "Child: 5\n\n" +
                        "3. calculateDirectly() without super: 30\n" +
                        "4. Calling parent method through child: Parent: 10");
    }

    // Additional tests for checking polymorphism with array
    @Test
    void polymorphismArrayExactTest() throws Exception {
        String input = "3\n" +
                "circle 5\n" +
                "rectangle 4 6\n" +
                "triangle 3 4 5";

        String expected = "Shape 0: Area = 78.54, Perimeter = 31.42\n" +
                "Shape 1: Area = 24, Perimeter = 20\n" +
                "Shape 2: Area = 6, Perimeter = 12\n" +
                "\n" +
                "Total area: 108.54\n" +
                "Total perimeter: 63.42";

        assertFileWithInput("polymorphism_array.php", input, expected);
    }

    @Test
    void polymorphismArraySimpleTest() throws Exception {
        String input = "1\ncircle 3";
        String expected = "Shape 0: Area = 28.27, Perimeter = 18.85\n" +
                "\n" +
                "Total area: 28.27\n" +
                "Total perimeter: 18.85";
        assertFileWithInput("polymorphism_array.php", input, expected);
    }

    @Test
    void visibilityExactTest() throws Exception {
        // Checking exact output for parent class
        String parentExpected = "=== Test in parent class ===\n" +
                "Inside class:\n" +
                "Public: public\n" +
                "Protected: protected\n" +
                "Private: private\n" +
                "Public method: public\n" +
                "Protected method: protected\n" +
                "Private method: private\n" +
                "\n" +
                "=== Direct access from outside ===\n" +
                "Public field: public\n" +
                "Public method: public\n";

        // Checking exact output for child class
        String childExpected = "=== Test in child class ===\n" +
                "In child class:\n" +
                "Public field: public\n" +
                "Protected field: protected\n" +
                "Private field: inaccessible\n" +
                "Calling parent protected method: protected";

        assertFileWithInput("visibility.php", "1", parentExpected);
        assertFileWithInput("visibility.php", "2", childExpected);
    }

    @Test
    void typeAdditionExactTest() throws Exception {
        // For number input "5"
        assertFileWithInput("type_addition.php", "5",
                "string: 15\n" +
                        "int: 15\n" +
                        "float: 15\n" +
                        "bool: 15\n" +
                        "string_concat: 510\n" +
                        "int_concat: 510\n" +
                        "float_concat: 510\n" +
                        "bool_concat: 510");
        
    }
}