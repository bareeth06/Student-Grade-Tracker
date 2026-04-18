import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

class Student {
    String name;
    int marks;

    Student(String name, int marks) {
        this.name = name;
        this.marks = marks;
    }
}

public class StudentGradeTrackerGUI extends JFrame {

    JTextField nameField, marksField;
    ArrayList<Student> students = new ArrayList<>();

    public StudentGradeTrackerGUI() {

        setTitle("🎓 Student Grade Tracker");
        setSize(400, 400);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(240, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // PANEL
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5,1,10,10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Enter Student Details"));

        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Marks:"));
        marksField = new JTextField();
        panel.add(marksField);

        JButton addBtn = new JButton("Add");
        addBtn.setBackground(new Color(70,130,180));
        addBtn.setForeground(Color.WHITE);
        panel.add(addBtn);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(panel, gbc);

        // BOTTOM BUTTONS
        JPanel bottom = new JPanel();
        JButton calcBtn = new JButton("Calculate");
        JButton reportBtn = new JButton("Summary");

        bottom.add(calcBtn);
        bottom.add(reportBtn);

        gbc.gridy = 1;
        add(bottom, gbc);

        // ADD FUNCTION
        addBtn.addActionListener(e -> {
            try {
                String name = nameField.getText();
                int marks = Integer.parseInt(marksField.getText());

                students.add(new Student(name, marks));

                showAutoCloseMessage("✅ Added Successfully!");

                nameField.setText("");
                marksField.setText("");

            } catch (Exception ex) {
                showAutoCloseMessage("❌ Invalid Input!");
            }
        });

        // CALCULATE
        calcBtn.addActionListener(e -> showStats());

        // SUMMARY
        reportBtn.addActionListener(e -> showSummary());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // AUTO CLOSE MESSAGE
    private void showAutoCloseMessage(String msg) {
        JDialog dialog = new JDialog(this, false);
        dialog.setSize(250, 100);
        dialog.setLayout(new BorderLayout());

        JLabel label = new JLabel(msg, SwingConstants.CENTER);
        dialog.add(label, BorderLayout.CENTER);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        new javax.swing.Timer(1500, e -> dialog.dispose()).start();
    }

    private String getGrade(int m) {
        if (m >= 90) return "A";
        else if (m >= 75) return "B";
        else if (m >= 50) return "C";
        else return "Fail";
    }

    private void showStats() {
        if (students.isEmpty()) {
            showAutoCloseMessage("No Data!");
            return;
        }

        int sum = 0, high = Integer.MIN_VALUE, low = Integer.MAX_VALUE;

        for (Student s : students) {
            sum += s.marks;
            if (s.marks > high) high = s.marks;
            if (s.marks < low) low = s.marks;
        }

        double avg = (double) sum / students.size();

        JOptionPane.showMessageDialog(this,
                "Average: " + avg +
                "\nHighest: " + high +
                "\nLowest: " + low);
    }

    private void showSummary() {
        if (students.isEmpty()) {
            showAutoCloseMessage("No Data!");
            return;
        }

        JFrame frame = new JFrame("📋 Summary");
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        String[] cols = {"Name", "Marks", "Grade"};
        DefaultTableModel m = new DefaultTableModel(cols, 0);

        for (Student s : students) {
            m.addRow(new Object[]{s.name, s.marks, getGrade(s.marks)});
        }

        JTable table = new JTable(m);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        // SEARCH
        JPanel top = new JPanel();
        JTextField searchField = new JTextField(10);
        JButton searchBtn = new JButton("Search");

        top.add(new JLabel("Search:"));
        top.add(searchField);
        top.add(searchBtn);

        frame.add(top, BorderLayout.NORTH);

        // DELETE
        JPanel bottom = new JPanel();
        JButton deleteBtn = new JButton("Delete");

        bottom.add(deleteBtn);
        frame.add(bottom, BorderLayout.SOUTH);

        // SEARCH FUNCTION
        searchBtn.addActionListener(e -> {
            String text = searchField.getText().toLowerCase();
            boolean found = false;

            for (int i = 0; i < m.getRowCount(); i++) {
                String name = m.getValueAt(i, 0).toString().toLowerCase();

                if (name.contains(text)) {
                    table.setRowSelectionInterval(i, i);
                    found = true;
                    break;
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(frame, "❌ Student not found");
            }
        });

        // DELETE FUNCTION
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(frame, "Select a row!");
                return;
            }

            students.remove(row);
            m.removeRow(row);

            showAutoCloseMessage("🗑 Deleted!");
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new StudentGradeTrackerGUI();
    }
}