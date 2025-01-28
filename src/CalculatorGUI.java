import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Stack;
public class CalculatorGUI extends JFrame implements ActionListener, KeyListener {
    private final JTextField display;
    private boolean isDarkMode = false;

    public CalculatorGUI() {
        Border border = BorderFactory.createLineBorder(Color.red);
        setTitle("SAM Calculator");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon ROG = new ImageIcon("img.png");
        setIconImage(ROG.getImage());

        display = new JTextField();
        display.setEditable(false);
        display.addKeyListener(this); // Add KeyListener to the display

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(border); // Set the border on the content panel
        contentPanel.add(display, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 4));
        String[] buttons = {"7", "8", "9", "/", "4", "5", "6", "*", "1", "2", "3", "-", "C", "0", "=", "+", "(", ")", ".", "Backspace"};

        for (String button : buttons) {
            JButton btn = new JButton(button);
            btn.addActionListener(this);
            buttonPanel.add(btn);
        }

        contentPanel.add(buttonPanel, BorderLayout.CENTER);

        JButton toggleButton = new JButton(" Dark/Light Mode");
        toggleButton.addActionListener(e -> toggleMode(contentPanel, buttonPanel));
        contentPanel.add(toggleButton, BorderLayout.SOUTH);

        setContentPane(contentPanel);
        setVisible(true);
    }

    private void toggleMode(JPanel contentPanel, JPanel buttonPanel) {
        isDarkMode = !isDarkMode;
        Color backgroundColor = isDarkMode ? Color.black : Color.white;
        Color textColor = isDarkMode ? Color.white : Color.black;

        contentPanel.setBackground(backgroundColor);
        buttonPanel.setBackground(backgroundColor);
        display.setBackground(backgroundColor);
        display.setForeground(textColor);

        Component[] components = buttonPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                component.setBackground(backgroundColor);
                component.setForeground(textColor);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.matches("[0-9]") || command.equals("(") || command.equals(")")) {
            display.setText(display.getText() + command);
        } else if (command.equals("C")) {
            display.setText("");
        } else if (command.equals("Backspace")) {
            String currentText = display.getText();
            if (currentText.length() > 0) {
                display.setText(currentText.substring(0, currentText.length() - 1));
            }
        } else if (command.equals("=")) {
            String expression = display.getText();
            try {
                double result = evaluateExpression(expression);
                display.setText(String.valueOf(result));
            } catch (Exception ex) {
                display.setText("Error");
            }
        } else if (command.equals(".")) {
            if (!display.getText().contains(".")) {
                display.setText(display.getText() + ".");
            }
        } else {
            display.setText(display.getText() + " " + command + " ");
        }
    }

    private double evaluateExpression(String expression) {
        Stack<Double> values = new Stack<>();
        Stack<Character> ops = new Stack<>();
        char[] tokens = expression.toCharArray();

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == ' ')
                continue;

            if (tokens[i] >= '0' && tokens[i] <= '9' || tokens[i] == '.') {
                StringBuilder sbuf = new StringBuilder();
                while (i < tokens.length && (tokens[i] >= '0' && tokens[i] <= '9' || tokens[i] == '.'))
                    sbuf.append(tokens[i++]);
                values.push(Double.parseDouble(sbuf.toString()));
                i--;
            } else if (tokens[i] == '(') {
                ops.push(tokens[i]);
            } else if (tokens[i] == ')') {
                while (ops.peek() != '(')
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                ops.pop();
            } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {
                while (!ops.empty() && hasPrecedence(tokens[i], ops.peek()))
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                ops.push(tokens[i]);
            }
        }

        while (!ops.empty())
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));

        return values.pop();
    }

    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')')
            return false;
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-'))
            return false;
        else
            return true;
    }

    private double applyOp(char op, double b, double a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0)
                    throw new UnsupportedOperationException("Cannot divide by zero");
                return a / b;
        }
        return 0;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char keyChar = e.getKeyChar();
        if (Character.isDigit(keyChar) || keyChar == '(' || keyChar == ')' || keyChar == '.') {
            display.setText(display.getText() + keyChar);
        } else if (keyChar == '+') {
            display.setText(display.getText() + " + ");
        } else if (keyChar == '-') {
            display.setText(display.getText() + " - ");
        } else if (keyChar == '*') {
            display.setText(display.getText() + " * ");
        } else if (keyChar == '/') {
            display.setText(display.getText() + " / ");
        } else if (keyChar == '\b') { // Backspace
            String currentText = display.getText();
            if (currentText.length() > 0) {
                display.setText(currentText.substring(0, currentText.length() - 1));
            }
        } else if (keyChar == '\n') { // Enter
            String expression = display.getText();
            try {
                double result = evaluateExpression(expression);
                display.setText(String.valueOf(result));
            } catch (Exception ex) {
                display.setText("Error");
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode >= KeyEvent.VK_NUMPAD0 && keyCode <= KeyEvent.VK_NUMPAD9) {
            display.setText(display.getText() + (keyCode - KeyEvent.VK_NUMPAD0));
        } else if (keyCode == KeyEvent.VK_ADD) {
            display.setText(display.getText() + " + ");
        } else if (keyCode == KeyEvent.VK_SUBTRACT) {
            display.setText(display.getText() + " - ");
        } else if (keyCode == KeyEvent.VK_MULTIPLY) {
            display.setText(display.getText() + " * ");
        } else if (keyCode == KeyEvent.VK_DIVIDE) {
            display.setText(display.getText() + " / ");
        } else if (keyCode == KeyEvent.VK_DECIMAL) {
            if (!display.getText().contains(".")) {
                display.setText(display.getText() + ".");
            }
        } else if (keyCode == KeyEvent.VK_BACK_SPACE) {
            String currentText = display.getText();
            if (!currentText.isEmpty()) {
                display.setText(currentText.substring(0, currentText.length() - 1));
            }
        } else if (keyCode == KeyEvent.VK_ENTER) {
            String expression = display.getText();
            try {
                double result = evaluateExpression(expression);
                display.setText(String.valueOf(result));
            } catch (Exception ex) {
                display.setText("Error");
            }
        }

    }
    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        new CalculatorGUI();
    }
}
