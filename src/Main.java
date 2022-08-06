import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class Game implements ActionListener {
    JFrame main = new JFrame("Motorola Memory Game");
    JPanel field = new JPanel();
    JPanel menu = new JPanel();
    JPanel menu2 = new JPanel();
    JPanel menu3 = new JPanel();
    JPanel mini = new JPanel();
    JPanel start_view = new JPanel();
    JPanel end_view = new JPanel();
    JButton btn[] = new JButton[20];
    JButton start = new JButton("Start");
    JButton exit = new JButton("Exit");
    JButton easy = new JButton("Easy");
    JButton hard = new JButton("Hard");
    JButton back = new JButton("Back to Main Menu");
    JLabel winner;
    Random random = new Random();
    boolean p = false;
    boolean show = true;
    boolean isEasy = true;
    int level = 0;
    int score = 0;
    int temp = 30;
    int temp2 = 30;
    String[] board;


    public Game() {
        main.setSize(500, 300);
        main.setLocation(500, 300);
        main.setLayout(new BorderLayout());
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        start_view.setLayout(new BorderLayout());
        menu.setLayout(new FlowLayout(FlowLayout.CENTER));
        menu2.setLayout(new FlowLayout(FlowLayout.CENTER));
        menu3.setLayout(new FlowLayout(FlowLayout.CENTER));
        mini.setLayout(new FlowLayout(FlowLayout.CENTER));
        start_view.add(menu, BorderLayout.NORTH);
        start_view.add(menu3, BorderLayout.CENTER);
        start_view.add(menu2, BorderLayout.SOUTH);
        menu3.add(mini, BorderLayout.CENTER);
        mini.add(easy, BorderLayout.NORTH);
        mini.add(hard, BorderLayout.NORTH);
        start.addActionListener(this);
        start.setEnabled(true);
        menu2.add(start);
        exit.addActionListener(this);
        exit.setEnabled(true);
        menu2.add(exit);
        easy.addActionListener(this);
        easy.setEnabled(true);
        hard.addActionListener(this);
        hard.setEnabled(true);
        back.addActionListener(this);
        back.setEnabled(true);
        main.add(start_view, BorderLayout.CENTER);
        main.setVisible(true);
    }
    public void initializeGame(boolean whatLevel) throws FileNotFoundException {
        int level = whatLevel ? 4 : 8;
        clearMain();
        board = new String[level * 2];
        for (int i = 0; i < level * 2; i++) {
            btn[i] = new JButton("");
            btn[i].setBackground(new Color(220, 220, 220));
            btn[i].addActionListener(this);
            btn[i].setEnabled(true);
            field.add(btn[i]);
        }
        Scanner s = new Scanner(new File("/Users/tomaszciula/projects/Motorola/Words.txt"));
        ArrayList<String> words = new ArrayList<>();
        while (s.hasNextLine()) {
            words.add(s.nextLine());
        }
        String[] a = words.toArray(String[]::new);
        for (int i = 0; i < level; i++) {
            for (int j = 0; j < 2; j++) {
                while (true) {
                    int y = random.nextInt(level * 2);
                    if (board[y] == null) {
                        btn[y].setText(a[i]);
                        board[y] = a[i];
                        break;
                    }
                }
            }
        }
        createBoard();
    }

    public void hideField(int level) {
        for (int i = 0; i < level * 2; i++) {
            btn[i].setText("");
        }
        show = false;
    }

    public void switchSpot(int i) {
        if (board[i] != "done") {
            if (btn[i].getText() == "") {
                btn[i].setText(board[i]);
            } else {
                btn[i].setText("");
            }
        }
    }

    public boolean checkWin() {
        for (int i = 0; i < (level * 2); i++) {
            if (board[i] != "done") return false;
        }
        winner();
        return true;
    }

    public void winner() {

        start_view.remove(field);
        start_view.add(end_view, BorderLayout.CENTER);
        end_view.add(new TextField("You Win"), BorderLayout.NORTH);
        end_view.add(new TextField("Score: " + score), BorderLayout.SOUTH);
        end_view.add(back);
        exit.setEnabled(true);
        exit.addActionListener(this);
    }

    public void goToMainScreen() {
        new Game();
    }

    public void createBoard() {
        field.setLayout(new BorderLayout());
        start_view.add(field, BorderLayout.CENTER);
        field.setLayout(new GridLayout(5, 4, 2, 2));
        field.setBackground(Color.black);
        field.requestFocus();
    }

    public void clearMain() {
        start_view.remove(menu);
        start_view.remove(menu2);
        start_view.remove(menu3);

        start_view.revalidate();
        start_view.repaint();
    }

    public void actionPerformed(ActionEvent click) {
        Object source = click.getSource();
        if (p) {
            switchSpot(temp2);
            switchSpot(temp);
            score++;
            temp = (level * 2);
            temp2 = 30;
            p = false;
        }
        if (source == start) {
            try {
                level = isEasy ? 4 : 8;
            } catch (Exception e) {
                level = 1;
            }

            try {
                initializeGame(isEasy);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        if (source == exit) {
            System.exit(0);
        }

        if (source == back) {
            main.dispose();
            goToMainScreen();
        }
        if (source == easy) {
            isEasy = true;
            easy.setForeground(Color.BLUE);
            hard.setForeground(Color.BLACK);
        } else if (source == hard) {
            isEasy = false;
            hard.setForeground(Color.BLUE);
            easy.setForeground(Color.BLACK);
        }

        for (int i = 0; i < (level * 2); i++) {
            if (source == btn[i]) {
                if (show) {
                    hideField(level);
                } else {//otherwise play
                    switchSpot(i);
                    if (temp >= (level * 2)) {
                        temp = i;
                    } else {
                        if ((board[temp] != board[i]) || (temp == i)) {
                            temp2 = i;
                            p = true;
                        } else {
                            board[i] = "done";
                            board[temp] = "done";
                            checkWin();
                            temp = (level * 2);
                        }

                    }
                }

            }


        }
    }


    public static void main(String[] args) {
        new Game();
    }
}