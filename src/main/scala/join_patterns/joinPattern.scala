package join_patterns

// Q = [A(42), B(21), A(84)]       | A(x) & B(y) & A(z)
//                 Msgs from Q     | Pattern Idxs from Pattern case
//                 [ Ø            -> {} ]
//                 [ {0}          -> { ([0], Map(x -> 42)), ([2], Map(z -> 42)) }]
//                 [ {0, 1}       -> { ([0, 1], Map(x -> 42, y -> 21)), ([2, 1], Map(z -> 42, y -> 21)) }]
//                 [ {1}          -> { ([1], Map(y -> 21)) }]
type NodeMapping = Map[List[Int], Set[(List[Int], Map[String, Any])]]
object NodeMapping:
  def apply(): Map[List[Int], Set[
    (List[Int], Map[String, Any])
  ]] = // TODO: Extend each nodemapping to maintain the substitutions of its current config.
    Map[List[Int], Set[(List[Int], Map[String, Any])]](List.empty -> Set.empty)

// Edges
//               { (Ø, {1}), ({1}, {1, 2}), ({1, 2}, Ø) }
type TreeEdges = Set[(List[Int], List[Int])]
object TreeEdges:
  def apply(): Set[(List[Int], List[Int])] =
    Set[(List[Int], List[Int])]()

case class MatchingTree(
    nodeMapping: NodeMapping = NodeMapping(),
    treeEdges: TreeEdges = TreeEdges()
) {
  def isEmpty: Boolean =
    nodeMapping.isEmpty && treeEdges.isEmpty
}

def printMapping(mapping: NodeMapping): Unit =
  mapping.foreach { (k, v) =>
    val kToStr = s"${k.mkString("[", ", ", "]")}"
    val vToStr = v
      .map((cd, fields) =>
        s"(${cd.mkString("[", ", ", "]")}, ${fields.map((k, v) => s"${k} -> ${v}").mkString("Map(", " , ", ")")})"
      )
      .mkString("{ ", ", ", " }")
    val mToStr = s"${kToStr}\t -> ${vToStr}"
    println(mToStr)
  }

/** An ADT defintion of a join pattern
  */
case class JoinPattern[M, T](
    extract: List[M] => (Option[List[M]], List[(Int, M)], Map[String, Any]),
    guard: Map[String, Any] => Boolean,
    rhs: Map[String, Any] => T,
    size: Int,
    partialExtract: (List[M], MatchingTree) => Option[MatchingTree]
)


enum AlgorithmType:
  case NaiveAlgorithm, BasicAlgorithm, TreeBasedAlgorithm