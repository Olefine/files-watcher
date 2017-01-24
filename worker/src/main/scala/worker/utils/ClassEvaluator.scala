package worker.utils

import com.twitter.util.Eval

object ClassEvaluator {
  def apply(classToEval: String): Function1[String, String] = {
    val eval = new Eval

    eval[Function[String, String]](classToEval)
  }
}