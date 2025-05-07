import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class PuzzleGame5 extends JFrame implements ActionListener {
    private final int SIZE = 3;
    private JButton[] buttons = new JButton[9];
    private int[] tiles = new int[9];
    private ImageIcon[] icons = new ImageIcon[9];
    private ImageIcon nineIcon;  // Gambar untuk tile kosong (diganti dengan angka 9)
    private JButton restartButton;
    private JButton solveButton;

    public PuzzleGame5() {
        setTitle("8 TileBound");
        setSize(320, 370);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initialize();
    }

    private void initialize() {
        loadImages();

        for (int i = 0; i < 9; i++) tiles[i] = i;
        shuffleTiles();

        setLayout(new BorderLayout());

        JPanel puzzlePanel = new JPanel(new GridLayout(SIZE, SIZE));
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            buttons[i].addActionListener(this);
            updateButton(i);
            buttons[i].setBorderPainted(false);
            buttons[i].setFocusPainted(false);
            puzzlePanel.add(buttons[i]);
        }
        add(puzzlePanel, BorderLayout.CENTER);

        restartButton = new JButton("Restart");
        restartButton.addActionListener(_ -> restartGame());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(restartButton);

        solveButton = new JButton("Solve");
        solveButton.addActionListener(_ -> solvePuzzle()); // Action for Solve button
        bottomPanel.add(solveButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadImages() {
        // Memuat gambar untuk tiles 1-8
        for (int i = 0; i < 9; i++) {
            String path = "res2/" + i + ".png"; // Gunakan gambar 0.png hingga 8.png
            icons[i] = new ImageIcon(path);
            Image img = icons[i].getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            icons[i] = new ImageIcon(img);
        }

        // Memuat gambar untuk tile kosong (angka 9)
        nineIcon = new ImageIcon("res2/9.png"); // Gambar untuk tile kosong diganti dengan angka 9
        Image img = nineIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        nineIcon = new ImageIcon(img);
    }

    private void updateButton(int i) {
        if (tiles[i] != 0) {
            buttons[i].setIcon(icons[tiles[i]]);
            buttons[i].setText(String.valueOf(tiles[i])); // Menampilkan angka pada tile
            buttons[i].setHorizontalTextPosition(SwingConstants.CENTER);
            buttons[i].setVerticalTextPosition(SwingConstants.CENTER);
            buttons[i].setFont(new Font("Arial", Font.BOLD, 20));
            buttons[i].setForeground(Color.WHITE);  // Menyesuaikan warna teks jika perlu
        } else {
            buttons[i].setIcon(null);  // Jangan menampilkan gambar pada tile kosong
            buttons[i].setText("");  // Menghapus angka pada tile kosong selama permainan berlangsung
        }
    }

    private void restartGame() {
        shuffleTiles();
        for (int i = 0; i < 9; i++) {
            updateButton(i);
        }
    }

    private void shuffleTiles() {
        do {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < 9; i++) list.add(i);
            Collections.shuffle(list);
            for (int i = 0; i < 9; i++) tiles[i] = list.get(i);
        } while (!isSolvable(tiles) || isSolved());
    }

    private boolean isSolvable(int[] puzzle) {
        int inv = 0;
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = i + 1; j < puzzle.length; j++) {
                if (puzzle[i] > 0 && puzzle[j] > 0 && puzzle[i] > puzzle[j]) inv++;
            }
        }
        return inv % 2 == 0;
    }

    private boolean isSolved() {
        for (int i = 0; i < 8; i++) {
            if (tiles[i] != i + 1) return false;
        }
        return tiles[8] == 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked = (JButton) e.getSource();
        int index = Arrays.asList(buttons).indexOf(clicked);
        int emptyIndex = findEmptyNeighbor(index);

        if (emptyIndex != -1) {
            int temp = tiles[index];
            tiles[index] = tiles[emptyIndex];
            tiles[emptyIndex] = temp;
            updateButton(index);
            updateButton(emptyIndex);

            if (isSolved()) {
                JOptionPane.showMessageDialog(this, "Selamat! Puzzle selesai!");

                // Menghilangkan angka dari semua tombol dan menampilkan gambar
                for (int i = 0; i < 9; i++) {
                    buttons[i].setText(""); // Menghapus angka
                    if (tiles[i] != 0) {
                        buttons[i].setIcon(icons[tiles[i]]);  // Menampilkan gambar untuk setiap tile
                    } else {
                        buttons[i].setIcon(nineIcon);  // Menampilkan gambar untuk tile kosong (angka 9)
                    }
                }
            }
        }
    }

    private int findEmptyNeighbor(int index) {
        int row = index / SIZE;
        int col = index % SIZE;
        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};

        for (int[] d : directions) {
            int newRow = row + d[0];
            int newCol = col + d[1];
            if (newRow >= 0 && newRow < SIZE && newCol >= 0 && newCol < SIZE) {
                int newIndex = newRow * SIZE + newCol;
                if (tiles[newIndex] == 0) return newIndex;
            }
        }
        return -1;
    }

    private void solvePuzzle() {
        // Menyelesaikan puzzle dengan mengatur tiles ke keadaan yang terpecahkan
        int[] solvedState = {1, 2, 3, 4, 5, 6, 7, 8, 0};
        for (int i = 0; i < 9; i++) {
            tiles[i] = solvedState[i];
            updateButton(i);
        }

        // Setelah puzzle terpecahkan, hilangkan angka dan tampilkan gambar
        for (int i = 0; i < 9; i++) {
            buttons[i].setText("");  // Menghapus angka
            if (tiles[i] != 0) {
                buttons[i].setIcon(icons[tiles[i]]);  // Menampilkan gambar untuk tile yang terpecahkan
            } else {
                buttons[i].setIcon(nineIcon);  // Menampilkan gambar untuk tile kosong (angka 9)
            }
        }

        JOptionPane.showMessageDialog(this, " YEAYYY Puzzle terpecahkan!");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PuzzleGame5 game = new PuzzleGame5();
            game.setVisible(true);
        });
    }
}
