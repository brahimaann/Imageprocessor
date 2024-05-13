import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ImageProcessor {
    private static BufferedImage image1, image2;
    private static Stack<BufferedImage> savedImage1, savedImage2;
    private static BufferedImage image;
    private static JComboBox<String> comboBox;
    private static JComboBox<String> imagecomboBox;
    private static JComboBox<String> arithmeticsComboBox;
    private static JComboBox<String> bitwiseComboBox;
    private static JComboBox<String> roiComboBox;
    private static JComboBox<String> filterComboBox;
    private static JComboBox<String> thresholdComboBox;


    public static void main(String[] args) {
        savedImage1 = new Stack<BufferedImage>();
        savedImage2 = new Stack<BufferedImage>();
        try {

            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file1 = fileChooser.getSelectedFile();


                image1 = ImageIO.read(file1);
                savedImage1.push(deepCopy(image1));
                image = image1;

                Image scaledImage1 = getScaledImage(image1, image1.getWidth(), image1.getHeight());


                JFrame frame = new JFrame("Image Processor");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JLabel label1 = new JLabel();

                JLabel label2 = new JLabel();
                frame.add(label1, BorderLayout.WEST);
                frame.add(label2, BorderLayout.EAST);
                frame.setSize(1200, 900);

                int labelWidth = 600;
                int labelHeight = 450;

                double scaleFactor = Math.min((double) labelWidth / image1.getWidth(), (double) labelHeight / image1.getHeight());


                label1.setIcon(new ImageIcon(image1.getScaledInstance((int) (image1.getWidth() * scaleFactor), (int) (image1.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));


                JMenuBar menubar = new JMenuBar();
                JMenu menu = new JMenu("menu");

                JPanel headerPanel = new JPanel();
                headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));

                menubar.add(menu);
                headerPanel.add(menubar);

                headerPanel.add(Box.createHorizontalGlue());


                JButton rescale = new JButton("Rescale");
                rescale.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String text = JOptionPane.showInputDialog(frame, "Enter the value:");
                        int[][][] img;
                        if (image.equals(image1)) {
                            savedImage1.push(deepCopy(image1));
                        } else if (image.equals(image2)) {
                            savedImage2.push(deepCopy(image2));
                        }
                        try {
                            float value = Float.parseFloat(text);
                            if (image.equals(image1)) {
                                rescaleImage(image1, value);
                                label1.setIcon(new ImageIcon(image1.getScaledInstance((int) (image1.getWidth() * scaleFactor), (int) (image1.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                            } else if (image.equals(image2)) {
                                rescaleImage(image2, value);
                                label2.setIcon(new ImageIcon(image2.getScaledInstance((int) (image2.getWidth() * scaleFactor), (int) (image2.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                            }
                        } catch (NumberFormatException ex) {

                        }
                    }
                });
                menu.add(rescale);

                JButton shift = new JButton("shift");
                shift.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String text = JOptionPane.showInputDialog(frame, "Enter the value:");
                        int[][][] img;
                        if (image.equals(image1)) {
                            savedImage1.push(deepCopy(image1));
                        } else if (image.equals(image2)) {
                            savedImage2.push(deepCopy(image2));
                        }
                        try {
                            int value = Integer.parseInt(text);
                            if (image.equals(image1)) {
                                shiftColorImage(image1, value);
                                label1.setIcon(new ImageIcon(image1.getScaledInstance((int) (image1.getWidth() * scaleFactor), (int) (image1.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                            } else if (image.equals(image2)) {
                                shiftColorImage(image2, value);
                                label2.setIcon(new ImageIcon(image2.getScaledInstance((int) (image2.getWidth() * scaleFactor), (int) (image2.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                            }
                        } catch (NumberFormatException ex) {

                        }
                    }
                });
                menu.add(shift);

                JButton noise = new JButton("noise");
                noise.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String text = JOptionPane.showInputDialog(frame, "Enter the value:");
                        int[][][] img;
                        if (image.equals(image1)) {
                            savedImage1.push(deepCopy(image1));
                        } else if (image.equals(image2)) {
                            savedImage2.push(deepCopy(image2));
                        }
                        try {
                            double value = Double.parseDouble(text);
                            if (image.equals(image1)) {
                                image1 =addSaltAndPepperNoise(image1, value);
                                label1.setIcon(new ImageIcon(image1.getScaledInstance((int) (image1.getWidth() * scaleFactor), (int) (image1.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                            } else if (image.equals(image2)) {
                                image2= addSaltAndPepperNoise(image2, value);
                                label2.setIcon(new ImageIcon(image2.getScaledInstance((int) (image2.getWidth() * scaleFactor), (int) (image2.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                            }
                        } catch (NumberFormatException ex) {

                        }
                    }
                });
                menu.add(noise);

                JButton shiftNrescale = new JButton("s&r");
                shiftNrescale.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (image.equals(image1)) {
                            savedImage1.push(deepCopy(image1));
                        } else if (image.equals(image2)) {
                            savedImage2.push(deepCopy(image2));
                        }

                            if (image.equals(image1)) {
                                image1 = shiftAndRescale(image1);
                                label1.setIcon(new ImageIcon(image1.getScaledInstance((int) (image1.getWidth() * scaleFactor), (int) (image1.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                            } else if (image.equals(image2)) {
                                image2 =shiftAndRescale(image2);
                                label2.setIcon(new ImageIcon(image2.getScaledInstance((int) (image2.getWidth() * scaleFactor), (int) (image2.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                            }
                    }
                });
                menu.add(shiftNrescale);

                JButton randLUT = new JButton("LUT");
                randLUT.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (image.equals(image1)) {
                            savedImage1.push(deepCopy(image1));
                        } else if (image.equals(image2)) {
                            savedImage2.push(deepCopy(image2));
                        }
                            if (image.equals(image1)) {
                               image1= applyRandomLookupTable(image1);
                                label1.setIcon(new ImageIcon(image1.getScaledInstance((int) (image1.getWidth() * scaleFactor), (int) (image1.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                            } else if (image.equals(image2)) {
                                image2 =applyRandomLookupTable(image2);
                                label2.setIcon(new ImageIcon(image2.getScaledInstance((int) (image2.getWidth() * scaleFactor), (int) (image2.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                            }

                    }
                });
                menu.add(randLUT);

                JButton bitSlice = new JButton("bitSlice");
                bitSlice.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (image.equals(image1)) {
                            savedImage1.push(deepCopy(image1));
                        } else if (image.equals(image2)) {
                            savedImage2.push(deepCopy(image2));
                        }
                        int value = -1;
                        while ((value<0||value>7)){
                        String text = JOptionPane.showInputDialog(frame, "Enter a value between 0 and 7:");
                        value = Integer.parseInt(text);
                        }
                        if (image.equals(image1)) {
                            image1= getBitPlane(image1, value);
                            label1.setIcon(new ImageIcon(image1.getScaledInstance((int) (image1.getWidth() * scaleFactor), (int) (image1.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                        } else if (image.equals(image2)) {
                            image2 =getBitPlane(image2, value);
                            label2.setIcon(new ImageIcon(image2.getScaledInstance((int) (image2.getWidth() * scaleFactor), (int) (image2.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                        }

                    }
                });
                menu.add(bitSlice);

                JButton convolute = new JButton("convolute");
                convolute.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (image.equals(image1)) {
                            savedImage1.push(deepCopy(image1));
                        } else if (image.equals(image2)) {
                            savedImage2.push(deepCopy(image2));
                        }

                        if (image.equals(image1)) {
                           image1=convolveColorImage(image1);
                            label1.setIcon(new ImageIcon(image1.getScaledInstance((int) (image1.getWidth() * scaleFactor), (int) (image1.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                        } else if (image.equals(image2)) {
                            image2 =convolveColorImage(image2);
                            label2.setIcon(new ImageIcon(image2.getScaledInstance((int) (image2.getWidth() * scaleFactor), (int) (image2.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                        }

                    }
                });
                menu.add(convolute);

                JButton histogramEqul = new JButton("HistogramEqualiser");
                histogramEqul.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (image.equals(image1)) {
                            savedImage1.push(deepCopy(image1));
                        } else if (image.equals(image2)) {
                            savedImage2.push(deepCopy(image2));
                        }

                        if (image.equals(image1)) {
                            image1= equalizeColorHistogram(image1);
                            label1.setIcon(new ImageIcon(image1.getScaledInstance((int) (image1.getWidth() * scaleFactor), (int) (image1.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                        } else if (image.equals(image2)) {
                            image2 =equalizeColorHistogram(image2);
                            label2.setIcon(new ImageIcon(image2.getScaledInstance((int) (image2.getWidth() * scaleFactor), (int) (image2.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                        }

                    }
                });
                menu.add(histogramEqul);

                JButton saveButton = new JButton("Save");
                menu.add(saveButton);
                saveButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        if (image.equals(image1)) {
                            saveImage(image1);
                            label1.setIcon(new ImageIcon(getScaledImage(image1, image1.getWidth(), image1.getHeight())));
                        } else if (image.equals(image2)) {
                            saveImage(image2);
                            label2.setIcon(new ImageIcon(getScaledImage(image2, image2.getWidth(), image2.getHeight())));
                        }
                    }
                });



                JButton loadButton = new JButton("Load");
                loadButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        loadImage(fileChooser, label2);
                        label2.setIcon(new ImageIcon(image2.getScaledInstance((int) (image2.getWidth() * scaleFactor), (int) (image2.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                    }
                });
                headerPanel.add(loadButton);

                JButton notButton = new JButton("Not");
                notButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (image.equals(image1)) {
                            savedImage1.push(deepCopy(image1));
                        } else if (image.equals(image2)) {
                            savedImage2.push(deepCopy(image2));
                        }
                        if (image.equals(image1)) {
                            image1 = bitwiseNot(image1);
                            label1.setIcon(new ImageIcon(image1.getScaledInstance((int) (image1.getWidth() * scaleFactor), (int) (image1.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                        } else if (image.equals(image2)) {
                            image2 = bitwiseNot(image2);
                            label2.setIcon(new ImageIcon(image2.getScaledInstance((int) (image2.getWidth() * scaleFactor), (int) (image2.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));

                        }
                    }
                });
                headerPanel.add(notButton);


                JMenuItem undoButton = new JMenuItem("Undo");
                undoButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {


                        if (image.equals(image1)) {
                            if (!savedImage1.isEmpty()) {
                                image1 = savedImage1.pop();
                                label1.setIcon(new ImageIcon(image1.getScaledInstance((int) (image1.getWidth() * scaleFactor), (int) (image1.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                                image = image1;
                            }
                        } else if (image.equals(image2)) {
                            if (!savedImage2.isEmpty()) {
                                image2 = savedImage2.pop();
                                label2.setIcon(new ImageIcon(image2.getScaledInstance((int) (image2.getWidth() * scaleFactor), (int) (image2.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                                image = image2;
                            }
                        }
                    }
                });
                menu.add(undoButton);



                String[] options = { "Original", "Negative", "logarithmic", "power law"};
                comboBox = new JComboBox<>(options);
                comboBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        if (image.equals(image1)) {
                            savedImage1.push(deepCopy(image1));
                        } else if (image.equals(image2)) {
                            savedImage2.push(deepCopy(image2));
                        }

                        String selectedOption = (String) comboBox.getSelectedItem();
                        if (selectedOption.equals("Negative")) {
                            if (image.equals(image1)) {
                                getNegative(image1);
                                label1.setIcon(new ImageIcon(image1.getScaledInstance((int) (image1.getWidth() * scaleFactor), (int) (image1.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                            } else if (image.equals(image2)) {
                                getNegative(image2);
                                label2.setIcon(new ImageIcon(image2.getScaledInstance((int) (image2.getWidth() * scaleFactor), (int) (image2.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                            }
                        } else if (selectedOption.equals("logarithmic")) {
                            String firstParameter = JOptionPane.showInputDialog(frame, "Enter the constant:");
                            int value = Integer.parseInt(firstParameter);
                            if (image.equals(image1)) {
                                image1 = logarithmic(image1, value);
                                label1.setIcon(new ImageIcon(image1.getScaledInstance((int) (image1.getWidth() * scaleFactor), (int) (image1.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                            } else if (image.equals(image2)) {
                                image2 = logarithmic(image2, value);
                                label2.setIcon(new ImageIcon(image2.getScaledInstance((int) (image2.getWidth() * scaleFactor), (int) (image2.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                            }
                        }
                        else if (selectedOption.equals("power law")) {
                            String firstParameter = JOptionPane.showInputDialog(frame, "Enter the constant:");
                            String secondParameter = JOptionPane.showInputDialog(frame, "Enter the power:");
                            double constant = Double.parseDouble(firstParameter);
                            double power = Double.parseDouble(secondParameter);
                            if (image.equals(image1)) {
                                image1 = powerLaw(image1, constant, power);
                                label1.setIcon(new ImageIcon(image1.getScaledInstance((int) (image1.getWidth() * scaleFactor), (int) (image1.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                            } else if (image.equals(image2)) {
                                image2 = powerLaw(image2, constant, power);
                                label2.setIcon(new ImageIcon(image2.getScaledInstance((int) (image2.getWidth() * scaleFactor), (int) (image2.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                            }
                        }
                        else if (selectedOption.equals("Original")) {
                            try {
                                if (image.equals(image1)) {
                                    image1 = ImageIO.read(file1);
                                    label1.setIcon(new ImageIcon(image1.getScaledInstance((int) (image1.getWidth() * scaleFactor), (int) (image1.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                                } else if (image.equals(image2)) {
                                    File file2 = fileChooser.getSelectedFile();
                                    image2 = ImageIO.read(file2);
                                    label2.setIcon(new ImageIcon(image2.getScaledInstance((int) (image2.getWidth() * scaleFactor), (int) (image2.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                                }
                            } catch (IOException ex) {
                                System.out.println("Error: " + ex.getMessage());
                            }
                        }


                    }
                });
                headerPanel.add(comboBox);
                frame.setVisible(true);

                String[] arithmetics = {"add", "subtract", "multiply", "divide"};
                arithmeticsComboBox = new JComboBox<>(arithmetics);
                arithmeticsComboBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        if (image.equals(image1)) {
                            savedImage1.push(deepCopy(image1));
                        } else if (image.equals(image2)) {
                            savedImage2.push(deepCopy(image2));
                        }

                        String selectedOption = (String) arithmeticsComboBox.getSelectedItem();
                        if (image2 != null) {
                            image1 = imageArithmetic(image1, image2, selectedOption);
                            label1.setIcon(new ImageIcon(image1.getScaledInstance((int) (image1.getWidth() * scaleFactor), (int) (image1.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                        }
                    }
                });
                headerPanel.add(arithmeticsComboBox);
                frame.setVisible(true);

                String[] filtering = {"min", "max", "midpoint", "median"};
                filterComboBox = new JComboBox<>(filtering);
                filterComboBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        if (image.equals(image1)) {
                            savedImage1.push(deepCopy(image1));
                        } else if (image.equals(image2)) {
                            savedImage2.push(deepCopy(image2));
                        }

                        String selectedOption = (String) filterComboBox.getSelectedItem();
                        if (selectedOption.equals("min")) {
                            if (image.equals(image1)) {
                                minFilter(image1);
                                label1.setIcon(new ImageIcon(image1.getScaledInstance((int) (image1.getWidth() * scaleFactor), (int) (image1.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                            } else if (image.equals(image2)) {
                                minFilter(image2);
                                label2.setIcon(new ImageIcon(image2.getScaledInstance((int) (image2.getWidth() * scaleFactor), (int) (image2.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                            }
                        } else if (selectedOption.equals("max")) {
                            if (image.equals(image1)) {
                                maxFilter(image1);
                                label1.setIcon(new ImageIcon(image1.getScaledInstance((int) (image1.getWidth() * scaleFactor), (int) (image1.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                            } else if (image.equals(image2)) {
                                maxFilter(image2);
                                label2.setIcon(new ImageIcon(image2.getScaledInstance((int) (image2.getWidth() * scaleFactor), (int) (image2.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                            }
                        }
                        else if (selectedOption.equals("midpoint")) {
                            if (image.equals(image1)) {
                                midpointFilter(image1);
                                label1.setIcon(new ImageIcon(image1.getScaledInstance((int) (image1.getWidth() * scaleFactor), (int) (image1.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                            } else if (image.equals(image2)) {
                                midpointFilter(image2);
                                label2.setIcon(new ImageIcon(image2.getScaledInstance((int) (image2.getWidth() * scaleFactor), (int) (image2.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                            }
                        }
                        else if (selectedOption.equals("median")) {

                                if (image.equals(image1)) {
                                    medianFilter(image1);
                                    label1.setIcon(new ImageIcon(image1.getScaledInstance((int) (image1.getWidth() * scaleFactor), (int) (image1.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                                } else if (image.equals(image2)) {
                                    medianFilter(image2);
                                    label2.setIcon(new ImageIcon(image2.getScaledInstance((int) (image2.getWidth() * scaleFactor), (int) (image2.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                                }

                        }


                    }
                });
                headerPanel.add(filterComboBox);
                frame.setVisible(true);

                String[] threshold = {"simple", "automated"};
                thresholdComboBox = new JComboBox<>(threshold);
                thresholdComboBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        if (image.equals(image1)) {
                            savedImage1.push(deepCopy(image1));
                        } else if (image.equals(image2)) {
                            savedImage2.push(deepCopy(image2));
                        }

                        String selectedOption = (String) thresholdComboBox.getSelectedItem();
                        if (selectedOption.equals("simple")) {
                            String firstParameter = JOptionPane.showInputDialog(frame, "Enter the value:");
                            int value = Integer.parseInt(firstParameter);
                            if (image.equals(image1)) {
                                image1 = simpleThreshold(image1, value);
                                label1.setIcon(new ImageIcon(image1.getScaledInstance((int) (image1.getWidth() * scaleFactor), (int) (image1.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                            } else if (image.equals(image2)) {
                                image2 = simpleThreshold(image2, value);
                                label2.setIcon(new ImageIcon(image2.getScaledInstance((int) (image2.getWidth() * scaleFactor), (int) (image2.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                            }
                        } else if (selectedOption.equals("automated")) {
                            if (image.equals(image1)) {
                                image1 = automatedThreshold(image1);
                                label1.setIcon(new ImageIcon(image1.getScaledInstance((int) (image1.getWidth() * scaleFactor), (int) (image1.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                            } else if (image.equals(image2)) {
                                image2 = automatedThreshold(image2);
                                label2.setIcon(new ImageIcon(image2.getScaledInstance((int) (image2.getWidth() * scaleFactor), (int) (image2.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                            }
                        }

                    }
                });
                headerPanel.add(thresholdComboBox);
                frame.setVisible(true);

                String[] bitwise = {"and", "or", "xor"};
                bitwiseComboBox = new JComboBox<>(bitwise);
                bitwiseComboBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        if (image.equals(image1)) {
                            savedImage1.push(deepCopy(image1));
                        } else if (image.equals(image2)) {
                            savedImage2.push(deepCopy(image2));
                        }

                        String selectedOption = (String) bitwiseComboBox.getSelectedItem();
                        if (image2 != null) {
                            image1 = bitwiseOperation(image1, image2, selectedOption);
                            label1.setIcon(new ImageIcon(image1.getScaledInstance((int) (image1.getWidth() * scaleFactor), (int) (image1.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                        }
                    }
                });
                headerPanel.add(bitwiseComboBox);
                frame.setVisible(true);

                String[] roiOp = {"and", "mulitiply"};
                roiComboBox = new JComboBox<>(roiOp);
                roiComboBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        if (image.equals(image1)) {
                            savedImage1.push(deepCopy(image1));
                        } else if (image.equals(image2)) {
                            savedImage2.push(deepCopy(image2));
                        }

                        String selectedOption = (String) roiComboBox.getSelectedItem();
                        if (image2 != null) {
                            image1 = applyROI(image1, image2, selectedOption);
                            label1.setIcon(new ImageIcon(image1.getScaledInstance((int) (image1.getWidth() * scaleFactor), (int) (image1.getHeight() * scaleFactor), Image.SCALE_SMOOTH)));
                        }
                    }
                });
                headerPanel.add(roiComboBox);
                frame.setVisible(true);


                String[] whichImage = {"image1", "image2"};
                imagecomboBox = new JComboBox<>(whichImage);
                imagecomboBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String selectedOption = (String) imagecomboBox.getSelectedItem();
                        if (selectedOption.equals("image1")) {
                            image = image1;
                        } else if (selectedOption.equals("image2")) {
                            image = image2;
                        }
                    }
                });
                headerPanel.add(imagecomboBox);
                frame.setVisible(true);

                frame.getContentPane().add(headerPanel, BorderLayout.PAGE_START);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public static int[] getHistogram(BufferedImage img) {
        int[] histogram = new int[256];
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int p = img.getRGB(x,y);
                int r = (p>>16)&0xff;
                int g = (p>>8)&0xff;
                int b = p&0xff;


                int max = Math.max(r, Math.max(g, b));
                histogram[max]++;
            }
        }
        return histogram;
    }

    public static int[] equalizeHistogram(int[] histogram) {

        int[] cdf = new int[histogram.length];
        cdf[0] = histogram[0];
        for (int i = 1; i < histogram.length; i++) {
            cdf[i] = cdf[i - 1] + histogram[i];
        }


        int cdfMin = 0;
        for (int i = 0; i < cdf.length; i++) {
            if (cdf[i] > 0) {
                cdfMin = cdf[i];
                break;
            }
        }


        int[] transform = new int[histogram.length];
        for (int i = 0; i < transform.length; i++) {
            transform[i] = Math.round(((float) (cdf[i] - cdfMin) / (cdf[cdf.length - 1] - cdfMin)) * (transform.length - 1));
        }

        return transform;
    }
    public static float[][] inputMatrix() {

        JTable table = new JTable(3, 3);


        JPanel panel = new JPanel();
        panel.add(new JScrollPane(table));


        int result = JOptionPane.showConfirmDialog(null, panel, "Enter matrix values", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {

            float[][] matrix = new float[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    Object value = table.getValueAt(i, j);
                    if (value != null) {
                        matrix[i][j] = Float.parseFloat(value.toString());
                    } else {

                        matrix[i][j] = 0;
                    }
                }
            }
            return matrix;
        } else {
            return null;
        }
    }

    public static BufferedImage shiftAndRescale(BufferedImage img) {
        Random rand = new Random();
        int shiftValue = rand.nextInt(-256, 256);

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {

                int pixel = img.getRGB(x, y);

                Color color = new Color(pixel, true);

                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                red = red + shiftValue;
                if (red > 255) {
                    red = 255;
                } else if (red < 0) {
                    red = 0;
                }
                green = green + shiftValue;
                if (green > 255) {
                    green = 255;
                } else if (green < 0) {
                    green = 0;
                }
                blue = blue + shiftValue;
                if (blue > 255) {
                    blue = 255;
                } else if (blue < 0) {
                    blue = 0;
                }
            }
        }
        float scaleFactor = rand.nextFloat(-0, 2);
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {

                int pixel = img.getRGB(x, y);

                Color color = new Color(pixel, true);

                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                red = (int) (red * scaleFactor);
                if (red > 255) {
                    red = 255;
                } else if (red < 0) {
                    red = 0;
                }
                green = (int) (green * scaleFactor);
                if (green > 255) {
                    green = 255;
                } else if (green < 0) {
                    green = 0;
                }
                blue = (int) (blue * scaleFactor);
                if (blue > 255) {
                    blue = 255;
                } else if (blue < 0) {
                    blue = 0;
                }

                color = new Color(red, green, blue);

                img.setRGB(x, y, color.getRGB());
            }
        }
        return img;
    }

    private static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    private static void loadImage(JFileChooser fileChooser, JLabel label) {
        try {
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();


                image2 = ImageIO.read(file);
                savedImage2.push(deepCopy(image2));


            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public static int[][][] imageToArray(BufferedImage img) {
        int height = img.getHeight();
        int width = img.getWidth();
        int[][][] imageArray = new int[height][width][3];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb = img.getRGB(j, i);
                imageArray[i][j][0] = (rgb >> 16) & 0xFF;
                imageArray[i][j][1] = (rgb >> 8) & 0xFF;
                imageArray[i][j][2] = rgb & 0xFF;
            }
        }
        return imageArray;
    }

    public static BufferedImage arrayToImage(int[][][] imageArray) {
        int height = imageArray.length;
        int width = imageArray[0].length;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int r = imageArray[i][j][0];
                int g = imageArray[i][j][1];
                int b = imageArray[i][j][2];
                int rgb = (r << 16) | (g << 8) | b;
                img.setRGB(j, i, rgb);
            }
        }
        return img;
    }

    private static void getNegative(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = img.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                r = 255 - r;
                g = 255 - g;
                b = 255 - b;

                p = (a << 24) | (r << 16) | (g << 8) | b;
                img.setRGB(x, y, p);
            }
        }
    }

    private static void saveImage(BufferedImage image) {
        try {
            JFileChooser fileChooser = new JFileChooser();

            int returnValue = fileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                ImageIO.write(image, "jpg", file);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static int[][] bufferedImageTo2darray(BufferedImage image) {
        int rows = image.getHeight();
        int cols = image.getWidth();
        int[][] resultImage = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int rgb = image.getRGB(j, i);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;
                resultImage[i][j] = (red + green + blue) / 3;
            }
        }

        return resultImage;
    }


    public static BufferedImage arrayTo2dImage(int[][] array) {
        int height = array.length;
        int width = array[0].length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int value = array[i][j];
                image.setRGB(j, i, (value << 16) | (value << 8) | value);
            }
        }

        return image;
    }

    private static Image getScaledImage(BufferedImage image, int targetWidth, int targetHeight) {
        int width = image.getWidth();
        int height = image.getHeight();

        double scaleFactor = .1;

        int scaledWidth = (int) (scaleFactor * width);
        int scaledHeight = (int) (scaleFactor * height);

        Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        BufferedImage bufferedScaledImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedScaledImage.createGraphics();
        g.drawImage(scaledImage, 0, 0, null);
        g.dispose();

        return bufferedScaledImage;
    }

    public static BufferedImage rescaleImage(BufferedImage img, float scaleFactor) {
        if (scaleFactor > 2.0 || scaleFactor < 0) {
            return img;
        }
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {

                int pixel = img.getRGB(x, y);

                Color color = new Color(pixel, true);

                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                red = (int) (red * scaleFactor);
                if (red > 255) {
                    red = 255;
                } else if (red < 0) {
                    red = 0;
                }
                green = (int) (green * scaleFactor);
                if (green > 255) {
                    green = 255;
                } else if (green < 0) {
                    green = 0;
                }
                blue = (int) (blue * scaleFactor);
                if (blue > 255) {
                    blue = 255;
                } else if (blue < 0) {
                    blue = 0;
                }

                color = new Color(red, green, blue);

                img.setRGB(x, y, color.getRGB());
            }
        }
        return img;
    }

    public static BufferedImage shiftColorImage(BufferedImage img, int shiftValue) {
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {

                int pixel = img.getRGB(x, y);

                Color color = new Color(pixel, true);

                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                red = red + shiftValue;
                if (red > 255) {
                    red = 255;
                } else if (red < 0) {
                    red = 0;
                }
                green = green + shiftValue;
                if (green > 255) {
                    green = 255;
                } else if (green < 0) {
                    green = 0;
                }
                blue = blue + shiftValue;
                if (blue > 255) {
                    blue = 255;
                } else if (blue < 0) {
                    blue = 0;
                }

                color = new Color(red, green, blue);

                img.setRGB(x, y, color.getRGB());
            }
        }
        return img;
    }

    public static BufferedImage imageArithmetic(BufferedImage img1, BufferedImage img2, String operation) {

        int[][][] image2 = imageToArray(img2);
        int[][][] image1 = imageToArray(img1);
        int rows = image1.length;
        int cols = image1[0].length;
        if (image1.length != image2.length || image1[0].length != image2[0].length) {
            System.out.println("Error: The two images have different dimensions!");
        } else {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    for (int k = 0; k < 3; k++) {
                        if (operation.equals("add")) {
                            image1[i][j][k] = image1[i][j][k] + image2[i][j][k];
                        } else if (operation.equals("subtract")) {
                            image1[i][j][k] = image1[i][j][k] - image2[i][j][k];
                        } else if (operation.equals("multiply")) {
                            image1[i][j][k] = image1[i][j][k] * image2[i][j][k];
                        } else if (operation.equals("divide")) {
                            boolean hasZero = false;
                            for (int a = 0; a < image2.length && !hasZero; a++) {
                                for (int b = 0; b < image2[i].length && !hasZero; b++) {
                                    for (int c = 0; c < 3 && !hasZero; c++) {
                                        if (image2[a][b][c] == 0) {
                                            hasZero = true;
                                        }
                                    }
                                }
                            }

                            if (hasZero) {
                                System.out.println("Error: The second image has pixel values equal to zero!");
                            } else {
                                image1[i][j][k] = image1[i][j][k] / image2[i][j][k];
                            }
                        }
                    }
                }
            }
        }
        img1 = arrayToImage(image1);

        return img1;
    }

    public static BufferedImage bitwiseNot(BufferedImage image) {
        int[][][] pixels = imageToArray(image);
        int rows = pixels.length;
        int cols = pixels[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                for (int k = 0; k < 3; k++) {
                    pixels[i][j][k] = ~pixels[i][j][k] & 0xFF;
                }
            }
        }
        image = arrayToImage(pixels);
        return image;
    }

    public static BufferedImage bitwiseOperation(BufferedImage img1, BufferedImage img2, String operation) {
        int[][][]image1 = imageToArray(img1);
        int[][][]image2 = imageToArray(img2);

        int rows = image1.length;
        int cols = image1[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                for (int k = 0; k < 3; k++) {
                    if (operation.equals("and")) {
                        image1[i][j][k] = image1[i][j][k] & image2[i][j][k];
                    } else if (operation.equals("or")) {
                        image1[i][j][k] = image1[i][j][k] | image2[i][j][k];
                    } else if (operation.equals("xor")) {
                        image1[i][j][k] = image1[i][j][k] ^ image2[i][j][k];
                    }
                }
            }
        }
        img1 = arrayToImage(image1);
        return img1;
    }

    public static BufferedImage applyROI(BufferedImage img1, BufferedImage img2, String operation) {
        int[][][] image = imageToArray(img1);
        int[][] roi = bufferedImageTo2darray(img2);
        int rows = image.length;
        int cols = image[0].length;



        if (operation.equals("and")) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    image[i][j][3] = roi[i][j] & 0xFF;
                }
            }
        } else if (operation.equals("multiply")) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    image[i][j][3] = roi[i][j] == 255 ? 1 : 0;
                }
            }
        }
        img1 = arrayToImage(image);

        return img1;
    }

    public static BufferedImage logarithmic(BufferedImage img, double c) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage result = new BufferedImage(width, height, img.getType());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = img.getRGB(x,y);
                int a = (p>>24)&0xff;
                int r = (p>>16)&0xff;
                int g = (p>>8)&0xff;
                int b = p&0xff;

                r = (int)(c * Math.log(1 + r));
                g = (int)(c * Math.log(1 + g));
                b = (int)(c * Math.log(1 + b));

                p = (a<<24) | (r<<16) | (g<<8) | b;
                result.setRGB(x, y, p);
            }
        }
        return result;
    }

    public static BufferedImage powerLaw(BufferedImage img, double c, double p) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage result = new BufferedImage(width, height, img.getType());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = img.getRGB(x,y);
                int a = (pixel>>24)&0xff;
                int r = (pixel>>16)&0xff;
                int g = (pixel>>8)&0xff;
                int b = pixel&0xff;

                r = (int)(c * Math.pow(r, p));
                g = (int)(c * Math.pow(g, p));
                b = (int)(c * Math.pow(b, p));

                pixel = (a<<24) | (r<<16) | (g<<8) | b;
                result.setRGB(x, y, pixel);
            }
        }
        return result;
    }

    public static BufferedImage applyRandomLookupTable(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage result = new BufferedImage(width, height, img.getType());


        int[] lut = new int[256];
        for (int i = 0; i < lut.length; i++) {
            lut[i] = (int)(Math.random() * 256);
        }


        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = img.getRGB(x,y);
                int a = (p>>24)&0xff;
                int r = (p>>16)&0xff;
                int g = (p>>8)&0xff;
                int b = p&0xff;

                r = lut[r];
                g = lut[g];
                b = lut[b];

                p = (a<<24) | (r<<16) | (g<<8) | b;
                img.setRGB(x, y, p);
            }
        }
        return img;
    }

    public static BufferedImage getBitPlane(BufferedImage img, int bit) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = img.getRGB(x,y);
                int r = (p>>16)&0xff;
                int g = (p>>8)&0xff;
                int b = p&0xff;


                int max = Math.max(r, Math.max(g, b));


                int bitValue = (max >> bit) & 1;


                if (bitValue == 1) {
                    result.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    result.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }
        return result;
    }

    public static BufferedImage equalizeColorHistogram(BufferedImage img) {

        int[][][] image = imageToArray(img);
        int[] redHistogram = new int[256];
        int[] greenHistogram = new int[256];
        int[] blueHistogram = new int[256];
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                redHistogram[image[i][j][0]]++;
                greenHistogram[image[i][j][1]]++;
                blueHistogram[image[i][j][2]]++;
            }
        }


        int[] redTransform = equalizeHistogram(redHistogram);
        int[] greenTransform = equalizeHistogram(greenHistogram);
        int[] blueTransform = equalizeHistogram(blueHistogram);


        int[][][] equalizedImage = new int[image.length][image[0].length][3];
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                equalizedImage[i][j][0] = redTransform[image[i][j][0]];
                equalizedImage[i][j][1] = greenTransform[image[i][j][1]];
                equalizedImage[i][j][2] = blueTransform[image[i][j][2]];
            }
        }

        img = arrayToImage(equalizedImage);
        return img;
    }

    public static BufferedImage convolveColorImage(BufferedImage img) {
        int[][][] imageArray = imageToArray(image);
        int width = imageArray[0].length;
        int height = imageArray.length;
        int[][][] resultArray = new int[height][width][3];

        float mask[][] = {{1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f}, {1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f}, {1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f}};

        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                for (int c = 0; c < 3; c++) {
                    float sum = 0.0f;
                    for (int a = 0; a < 3; a++) {
                        for (int b = 0; b < 3; b++) {
                            int xn = x + a - 1;
                            int yn = y + b - 1;
                            sum += imageArray[yn][xn][c] * mask[a][b];
                        }
                    }
                    resultArray[y][x][c] = Math.min(Math.max(Math.round(sum), 0), 255);
                }
            }
        }

        return arrayToImage(resultArray);
    }

    public static BufferedImage addSaltAndPepperNoise(BufferedImage image, double noiseProbability) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, image.getType());

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (Math.random() < noiseProbability) {
                    if (Math.random() < 0.5) {
                        result.setRGB(x, y, Color.WHITE.getRGB());
                    } else {
                        result.setRGB(x, y, Color.BLACK.getRGB());
                    }
                } else {
                    result.setRGB(x, y, image.getRGB(x, y));
                }
            }
        }

        return result;
    }

    public static void minFilter(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, image.getType());
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                int min = Integer.MAX_VALUE;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        Color color = new Color(image.getRGB(x + i, y + j));
                        int grayValue = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                        if (grayValue < min) {
                            min = grayValue;
                        }
                    }
                }
                result.setRGB(x, y, new Color(min, min, min).getRGB());
            }
        }
        Graphics g = image.getGraphics();
        g.drawImage(result, 0, 0, null);
        g.dispose();
    }

    public static void maxFilter(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, image.getType());
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                int max = Integer.MIN_VALUE;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        Color color = new Color(image.getRGB(x + i, y + j));
                        int grayValue = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                        if (grayValue > max) {
                            max = grayValue;
                        }
                    }
                }
                result.setRGB(x, y, new Color(max, max, max).getRGB());
            }
        }
        Graphics g = image.getGraphics();
        g.drawImage(result, 0, 0, null);
        g.dispose();
    }

    public static void midpointFilter(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, image.getType());
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                int min = Integer.MAX_VALUE;
                int max = Integer.MIN_VALUE;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        Color color = new Color(image.getRGB(x + i, y + j));
                        int grayValue = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                        if (grayValue < min) {
                            min = grayValue;
                        }
                        if (grayValue > max) {
                            max = grayValue;
                        }
                    }
                }
                int midpoint = (min + max) / 2;
                result.setRGB(x, y, new Color(midpoint, midpoint, midpoint).getRGB());
            }
        }
        Graphics g = image.getGraphics();
        g.drawImage(result, 0, 0, null);
        g.dispose();
    }

    public static void medianFilter(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, image.getType());
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                int[] neighborhood = new int[9];
                int index = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        Color color = new Color(image.getRGB(x + i, y + j));
                        int grayValue = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                        neighborhood[index] = grayValue;
                        index++;
                    }
                }
                Arrays.sort(neighborhood);
                int median = neighborhood[4];
                result.setRGB(x, y, new Color(median, median, median).getRGB());
            }
        }
        Graphics g = image.getGraphics();
        g.drawImage(result, 0, 0, null);
        g.dispose();
    }

    public static BufferedImage simpleThreshold(BufferedImage image, int threshold) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = new Color(image.getRGB(x, y));
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                int avg = (red + green + blue) / 3;
                if (avg >= threshold) {
                    result.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    result.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }
        return result;
    }

    public static BufferedImage automatedThreshold(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);


        int[] histogram = new int[256];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = new Color(image.getRGB(x, y));
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                int avg = (red + green + blue) / 3;
                histogram[avg]++;
            }
        }


        int threshold = 0;
        for (int i = 0; i < 256; i++) {
            threshold += i * histogram[i];
        }
        threshold /= width * height;

        boolean done = false;
        while (!done) {
            int t1 = 0;
            int t2 = 0;
            int m1 = 0;
            int m2 = 0;

            for (int i = 0; i < threshold; i++) {
                t1 += histogram[i] * i;
                m1 += histogram[i];
            }

            for (int i = threshold; i < 256; i++) {
                t2 += histogram[i] * i;
                m2 += histogram[i];
            }

            if (m1 == 0 || m2 == 0) {
                break;
            }

            t1 /= m1;
            t2 /= m2;

            if (threshold == (t1 + t2) / 2) {
                done = true;
            } else {
                threshold = (t1 + t2) / 2;
            }
        }


        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = new Color(image.getRGB(x, y));
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                int avg = (red + green + blue) / 3;
                if (avg >= threshold) {
                    result.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    result.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }

        return result;
    }


}