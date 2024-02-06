package sharedstuff.domain

/**
 * We need to be able to pass the statement and incomplete implementation for display
 * and after receiving result from user check if it compiles and if it works as expected
 * using testing statement and result
 *
 * Example
 *
 * id 0
 * statement "Complete function to sum two Int numbers"
 * incompleteImplementation "def sum(a: Int, b: Int): Int ="
 * correctImplementation "a + b"
 * testingStatement "sum(2,3)"
 * testingResult "5"
 * hint "sum(1,1) should return 2"
 *
 * */

case class Problem(id: Int,
                   statement: String,
                   incompleteImplementation: String,
                   correctImplementation: String,
                   testingStatement: String,
                   testingResult: String,
                   hint: String)
