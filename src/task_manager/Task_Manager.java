package task_manager;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

public class Task_Manager {

    public static void main(String[] args) {
        ArrayList<String> taskList = new ArrayList<>();
        JFrame frame = new JFrame("Task Manager for Students");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Title
        gbc.gridwidth = 3;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel title = new JLabel("Task Manager", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        frame.add(title, gbc);

        // Task Panel inside ScrollPane
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        JPanel taskPanel = new JPanel(new GridBagLayout());
        JScrollPane scrollPane = new JScrollPane(taskPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        frame.add(scrollPane, gbc);

        // Input and Add Button
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;

        JPanel inputPanel = new JPanel(new FlowLayout());
        JTextField taskField = new JTextField(20);
        JButton addButton = new JButton("Add Task");

        // NEW: Add Date Picker (JSpinner)
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);

        inputPanel.add(taskField);
        inputPanel.add(dateSpinner);  // Add date picker to input panel
        inputPanel.add(addButton);
        frame.add(inputPanel, gbc);


        // Save Button
        gbc.gridy = 3;
        JButton saveButton = new JButton("Save");
        frame.add(saveButton, gbc);

        // Track row count for GridBag
        final int[] rowCount = {0};

        // Add Task Button Logic
        addButton.addActionListener(e -> {
            String taskText = taskField.getText().trim();
            String taskDate = dateEditor.getFormat().format(dateSpinner.getValue()); // Get selected date

            if (!taskText.isEmpty()) {
                String fullTask = taskText + " [Due: " + taskDate + "]";
                taskList.add(fullTask);  // Save the combined task and date

                GridBagConstraints rowGbc = new GridBagConstraints();
                rowGbc.insets = new Insets(5, 5, 5, 5);
                rowGbc.gridy = rowCount[0];

                rowGbc.gridx = 0;
                JButton deleteButton = new JButton("Trash");
                taskPanel.add(deleteButton, rowGbc);

                rowGbc.gridx = 1;
                JLabel taskLabel = new JLabel(taskText);
                taskPanel.add(taskLabel, rowGbc);

                rowGbc.gridx = 2;
                JCheckBox doneCheck = new JCheckBox(taskDate);  // Show date as label next to checkbox
                taskPanel.add(doneCheck, rowGbc);

                deleteButton.addActionListener(ev -> {
                    taskPanel.remove(deleteButton);
                    taskPanel.remove(taskLabel);
                    taskPanel.remove(doneCheck);
                    taskList.remove(fullTask);
                    taskPanel.revalidate();
                    taskPanel.repaint();
                });

                rowCount[0]++;
                taskField.setText("");
                taskPanel.revalidate();
                taskPanel.repaint();
            }
        });


        // Save Button Logic
        saveButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Tasks To File");

            int userSelection = fileChooser.showSaveDialog(frame);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File fileToSave = fileChooser.getSelectedFile();

                // If user didn't include ".txt", add it
                if (!fileToSave.getName().toLowerCase().endsWith(".txt")) {
                    fileToSave = new java.io.File(fileToSave.getAbsolutePath() + ".txt");
                }

                try {
                    FileWriter writer = new FileWriter(fileToSave, false); // overwrite mode
                    for (String task : taskList) {
                        writer.write(task + "\n");
                        writer.write("----------------------------\n");
                    }
                    writer.close();
                    JOptionPane.showMessageDialog(frame, "Tasks saved to: " + fileToSave.getAbsolutePath());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error saving file.");
                    ex.printStackTrace();
                }
            }
        });


        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
