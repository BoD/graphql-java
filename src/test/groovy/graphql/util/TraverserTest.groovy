package graphql.util

import spock.lang.Specification

class TraverserTest extends Specification {
    class Node {
        int number;
        List<Node> children = Collections.emptyList();
    }

    def "test pre-order depth-first traversal"() {
        given:
            Node root = new Node(number: 0, children: [
                new Node(number: 1, children: [
                    new Node(number: 3)
                ]),
                new Node(number: 2, children: [
                    new Node(number: 4),
                    new Node(number: 5)
                ])
            ])
        def initialData = new ArrayList();
        when:
        Traverser.depthFirst({ Node n -> n.children }, initialData)
                .traverse(root, new TraverserVisitor<Node>() {
            public TraversalControl enter(TraverserContext<? super Node> context) {
                context.getInitialData().add(context.thisNode().number);
                return TraversalControl.CONTINUE;
                    }

            public TraversalControl leave(TraverserContext<? super Node> context) {
                return TraversalControl.CONTINUE;
                    }
                })

        then:
        initialData == [0, 1, 3, 2, 4, 5]
    }

    def "test post-order depth-first traversal"() {
        given:
            Node root = new Node(number: 0, children: [
                new Node(number: 1, children: [
                    new Node(number: 3)
                ]),
                new Node(number: 2, children: [
                    new Node(number: 4),
                    new Node(number: 5)
                ])
            ])
        def initialData = new ArrayList();
        when:
        Traverser.depthFirst({ Node n -> n.children }, initialData)
                .traverse(root, new TraverserVisitor<Node>() {
            public TraversalControl enter(TraverserContext<? super Node> context) {
                return TraversalControl.CONTINUE
                    }

            public TraversalControl leave(TraverserContext<? super Node> context) {
                context.getInitialData().add(context.thisNode().number);
                return TraversalControl.CONTINUE;
                    }
                })

        then:
        initialData == [3, 1, 4, 5, 2, 0]
    }

    def "test breadth-first traversal"() {
        given:
            Node root = new Node(number: 0, children: [
                new Node(number: 1, children: [
                    new Node(number: 3)
                ]),
                new Node(number: 2, children: [
                    new Node(number: 4),
                    new Node(number: 5)
                ])
            ])
        def initialData = new ArrayList()
        when:
        Traverser.breadthFirst({ Node n -> n.children }, initialData)
                .traverse(root, new TraverserVisitor<Node>() {
            public TraversalControl enter(TraverserContext<? super Node> context) {
                context.getInitialData().add(context.thisNode().number);
                return TraversalControl.CONTINUE;
                    }

            public TraversalControl leave(TraverserContext<? super Node> context) {
                return TraversalControl.CONTINUE;
                    }
                })

        then:
        initialData == [0, 1, 2, 3, 4, 5]
    }
}

