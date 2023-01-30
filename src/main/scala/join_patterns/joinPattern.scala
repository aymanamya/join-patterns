package join_patterns

//                 Msgs from Q     | Pattern Idxs from Pattern case
//                 [ Ø            -> {} ]
//                 [ {0}          -> { [0], [2] }]
//                 [ {0, 1}       -> { [0, 1], [2, 1] }]
//                 [ {1}          -> { [1] }]
type NodeMapping = Map[List[Int], Set[List[Int]]]
object NodeMapping:
  def apply() : Map[List[Int], Set[List[Int]]] =
    Map[List[Int], Set[List[Int]]]()

// Edges
//               { (Ø, {1}), ({1}, {1, 2}), ({1, 2}, Ø) }
type TreeEdges = Set[Tuple2[Set[Int], Set[Int]]]
object TreeEdges:
  def apply() : Set[Tuple2[Set[Int], Set[Int]]] =
    Set[Tuple2[Set[Int], Set[Int]]]()


case class MatchingTree(
  nodeMapping : NodeMapping = NodeMapping(),
  treeEdges : TreeEdges = TreeEdges()
) {
  def isEmpty : Boolean =
    nodeMapping.isEmpty && treeEdges.isEmpty
}



/** An ADT defintion of a join pattern
  *
  * */
case class JoinPattern[M, T](
    extract: List[M] => (Option[List[M]], List[(Int, M)], Map[String, Any]),
    guard: Map[String, Any] => Boolean,
    rhs: Map[String, Any] => T,
    size: Int,
    partialExtract : (List[M], MatchingTree) => Option[MatchingTree]
)