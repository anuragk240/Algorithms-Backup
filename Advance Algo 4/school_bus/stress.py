# python2
import time
import random
from operator import itemgetter
from itertools import permutations, combinations


INF = 10 ** 9


def read_data():
    n, m = map(int, raw_input().split())
    graph = [[INF] * n for _ in range(n)]
    for _ in range(m):
        u, v, weight = map(int, raw_input().split())
        u -= 1
        v -= 1
        graph[u][v] = graph[v][u] = weight
    return graph


def create_graph(n, dist3):
    graph = [[INF] * n for _ in range(n)]
    for d3 in dist3:
        u = d3[0] - 1
        v = d3[1] - 1
        weight = d3[2]
        graph[u][v] = graph[v][u] = weight
    return graph


def print_answer(path_weight, path, all_paths=False):
    print(path_weight)
    if path_weight == -1:
        return

    if not all_paths:
        print(' '.join(map(str, path)))
    else:
        for p in path:
            print(' '.join(map(str, p)))


def optimal_path(graph, all_paths=False):
    # This solution tries all the possible sequences of stops.
    # It is too slow to pass the problem.
    # Implement a more efficient algorithm here.
    n = len(graph)
    best_ans = INF
    best_path = []

    for p in permutations(range(n)):
        if p[0] != 0:
            break
        cur_sum = 0
        for i1 in range(1, n):
            if graph[p[i1 - 1]][p[i1]] == INF:
                break
            cur_sum += graph[p[i1 - 1]][p[i1]]
        else:
            if graph[p[-1]][p[0]] == INF:
                continue
            cur_sum += graph[p[-1]][p[0]]
            if cur_sum <= best_ans:
                best_ans = cur_sum
                if not all_paths:
                    best_path = list(p)
                else:
                    if cur_sum < best_ans:
                        best_path = list()
                    best_path.append(list(p))

    if best_ans == INF:
        return -1, []

    for b in best_path:
        for i1 in range(0, len(b)):
            b[i1] += 1

    return best_ans, best_path


if __name__ == '__main__':

    compare = True
    try_number = 1
    success = 1000  # tries to success
    max_dist = 10 ** 1
    max_nodes = 5

    while compare and try_number <= success:
        print ('\n')
        print('==== Try Number: {} ===='.format(try_number))

        print('=== INPUT ===')
        n1 = random.randint(2, max_nodes)
        m1 = random.randint(1, n1*(n1-1)/2)
        # override
        # n1 = 4
        # m1 = 4
        print('n: {} m: {}'.format(n1, m1))

        print('distances:')
        nodes = list(range(1, n1+1))
        dist = list()
        for d in combinations(nodes, 2):
            dist.append(d)
        len_dist = len(dist)

        dist2 = list()
        randoms = list()
        i = 0
        while i < m1:
            x1 = random.randint(0, len_dist-1)
            if x1 in randoms:
                continue
            randoms.append(x1)
            i += 1

            x2 = list(dist[x1])
            t = random.randint(1, max_dist)
            x2.append(t)
            dist2.append(x2)
        sorted_dist = sorted(dist2, key=itemgetter(0, 1))
        # override
        # sorted_dist = [[1, 2, 1], [1, 4, 2], [2, 3, 2], [3, 4, 6]]

        for d in sorted_dist:
            for x in d:
                print('{}'.format(x)),
            print

        print ('\n')
        print('======= OUTPUT =======')
        print('=== SLOW ====')
        start = time.time()
        best, best_list = optimal_path(create_graph(n1, sorted_dist), True)
        end = time.time()
        print('=== SLOW time: {} seconds ==='.format(end - start))
        print('= Results: =')
        print_answer(best, best_list, True)

        print('=== DYNAMIC ====')
        start = time.time()
        clock = 1
        
        # HERE YOU SHOULD IMPLEMENT YOUR TSP FUNCTION
        best_d, best_list_d = tsp(create_graph(n1, sorted_dist))
        end = time.time()
        print('=== DYNAMIC time: {} seconds ==='.format(end - start))
        print('= Results: =')
        print_answer(best_d, best_list_d)

        if best == best_d:
            if best != -1:
                compare = False
                for b_list in best_list:
                    if b_list == best_list_d:
                        compare = True
                        break
        else:
            compare = False

        if not compare:
            print('===== NO COMPARE ======')

        try_number += 1

    if try_number == success + 1:
        print ('\n')
        print('=====SUCCESS AFTER {} TRIES =================='.format(try_number-1))
