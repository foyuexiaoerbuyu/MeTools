package com;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SwingLayoutExample extends JFrame {
    private JComboBox<String> categoryComboBox;
    private JTextField searchField;
    private JButton searchButton;
    private JCheckBox checkBox;
    private JPanel buttonPanel;
    private JScrollPane scrollPane;
    private JList<String> itemList;

    public SwingLayoutExample() {
        setTitle("Swing Layout Example");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // First row with input elements
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        searchField = new JTextField();
        c.weightx = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        topPanel.add(searchField, c);

        String[] categories = { "Category 1", "Category 2", "Category 3" };
        categoryComboBox = new JComboBox<String>(categories);
        c.weightx = 0.0;
        c.gridx = 1;
        topPanel.add(categoryComboBox, c);

        // Add listener for combo box item clicks
        categoryComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle click on combo box item here
                System.out.println("Combo box item clicked: " + categoryComboBox.getSelectedItem().toString());
            }
        });

        searchButton = new JButton("Search");
        c.gridx = 2;
        topPanel.add(searchButton, c);

        // Second row with check box and dynamic buttons
        checkBox = new JCheckBox("Option");
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addButton("Button 1");
        addButton("Button 2");


        // Add listeners for search button and search field
        addListeners();

        JPanel middlePanel = new JPanel(new GridBagLayout());
        c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        middlePanel.add(checkBox, c);

        c.gridx = 1;
        c.gridy = 0;
        middlePanel.add(buttonPanel, c);

        // Third row with list of items
        ArrayList<String> itemArrayList = new ArrayList<String>();
        for (int i = 1; i <= 20; ++i) {
            itemArrayList.add("Item " + i);
        }
        String[] items = itemArrayList.toArray(new String[itemArrayList.size()]);
        itemList = new JList<String>(items);
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane = new JScrollPane(itemList);

        // Add listener for list item clicks
        itemList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int index = itemList.getSelectedIndex();
                    System.out.println("Selected item: " + index);
                    // Handle click on list item here
                }
            }
        });

        // Add rows to main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.weightx = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        mainPanel.add(topPanel, c);

        c.gridx = 0;
        c.gridy = 1;
        mainPanel.add(middlePanel, c);

        c.gridx = 0;
        c.gridy = 2;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        mainPanel.add(scrollPane, c);

        // Add vertical and horizontal struts to align rows
        mainPanel.add(Box.createVerticalStrut(10), new GridBagConstraints(0, 1, 1, 1, 0.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        mainPanel.add(Box.createHorizontalStrut(10), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));


        setContentPane(mainPanel);
    }

    private void addButton(String label) {
        JButton button = new JButton(label);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle click on dynamic button here
                System.out.println("Button clicked: " + label);
            }
        });
        buttonPanel.add(button);
    }

    private void addListeners() {
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle click on search button here
//                String sysClipboardText = WinUtils.getSysClipboardText();
                System.out.println("Search button clicked");
            }
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                // Handle input change in search field here
                System.out.println("Search field input: " + searchField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                // Handle input change in search field here
                System.out.println("Search field input: " + searchField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Not used for plain text components
            }
        });

        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle checkbox selection change here
                if (checkBox.isSelected()) {
                    System.out.println("Checkbox selected");
                } else {
                    System.out.println("Checkbox deselected");
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SwingLayoutExample example = new SwingLayoutExample();
                example.setVisible(true);
            }
        });
    }
}
