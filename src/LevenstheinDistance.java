class LevenstheinDistance {
    static int indexOfMinimum(int[] arr) {
        int min = Integer.MAX_VALUE;
        int index = -1;
        for (int i = 0; i<arr.length;i++) {
            if(arr[i]<min) {
                min = arr[i];
                index = i;
            }
        }
        return index;
    }

    private static int minimum(int i1, int i2, int i3) {
        if (Math.min(i1,i2)<i3) return Math.min(i1,i2);
        else return i3;
    }

    static int computeLevenshteinDistance(CharSequence lhs, CharSequence rhs) {
        int[][] distance = new int[lhs.length() + 1][rhs.length() + 1];

        for (int i = 0; i <= lhs.length(); i++)
            distance[i][0] = i;
        for (int j = 1; j <= rhs.length(); j++)
            distance[0][j] = j;

        for (int i = 1; i <= lhs.length(); i++)
            for (int j = 1; j <= rhs.length(); j++)
                distance[i][j] = minimum(
                        distance[i - 1][j] + 1,
                        distance[i][j - 1] + 1,
                        distance[i - 1][j - 1] + ((lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1));

        return distance[lhs.length()][rhs.length()];
    }
}