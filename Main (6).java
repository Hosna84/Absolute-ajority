import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static int n;
    static int[] Adad = new int[300000];
    static barg[] tree = new barg[4 * 300000];

    static class barg {
        int candidate;
        int count;
        int idx;
        barg left;
        barg right;

        barg(int candidate, int count, int idx, barg left, barg right) {
            this.candidate = candidate;
            this.count = count;
            this.idx = idx;
        }
    }

    static barg merge(barg x, barg y) {
        if (x.candidate == y.candidate)
            return new barg(x.candidate, x.count + y.count, x.idx / 2, x, y);

        else if (x.count > y.count)
            return new barg(x.candidate, x.count - y.count, x.idx / 2, x, y);

        else return new barg(y.candidate, y.count - x.count, x.idx / 2, x, y);
    }

    static void build(int i, int l, int r) {
        if (l == r) {
            tree[i] = new barg(Adad[l], 1, i, null, null);
            return;
        }

        int mid = (l + r) / 2;

        build(2 * i, l, mid);
        build(2 * i + 1, mid + 1, r);

        tree[i] = merge(tree[2 * i], tree[2 * i + 1]);
    }

    static barg query(int i, int l, int r, int leftQuery, int rightQuery) {
        if (l > rightQuery || r < leftQuery) return new barg(-1, 0, -10000, null, null);
        if (l >= leftQuery && r <= rightQuery) return tree[i];
        int mid = (l + r) / 2;
        return merge(query(2 * i, l, mid, leftQuery, rightQuery), query(2 * i + 1, mid + 1, r, leftQuery, rightQuery));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        int query = scanner.nextInt();
        List<Integer>[] position = new List[300000];
        for (int i = 0; i < 300000; i++)
            position[i] = new ArrayList<>();
        for (int i = 0; i < 300000; i++)
            position[i].add(-1);

        for (int i = 0; i < n; i++) {
            Adad[i] = scanner.nextInt();
            position[Adad[i]].add(i);
        }
        build(1, 0, n - 1);
        for (int i = 0; i < query; i++) {
            int l = scanner.nextInt() - 1;
            int r = scanner.nextInt() - 1;
            if (l < 0)
                l = 0;
            if (r > n - 1)
                r = n - 1;
            int candidate = query(1, 0, n - 1, l, r).candidate;

            if (candidate < 0) {
                System.out.println("0");
                continue;
            }

            int lb = lowerBound(position[candidate], l) - 1;
            int ub = upperBound(position[candidate], r) - 1;

            int freq = ub - lb;

            if (freq * 2 > r - l + 1)
                System.out.println(candidate);
            else
                System.out.println("0");
        }
    }

    static int lowerBound(List<Integer> list, int value) {
        int left = 0;
        int right = list.size();
        while (left < right) {
            int mid = (left + right) / 2;
            if (list.get(mid) < value) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    static int upperBound(List<Integer> list, int value) {
        int left = 0;
        int right = list.size();
        while (left < right) {
            int mid = (left + right) / 2;
            if (list.get(mid) <= value) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }
}