package org.yifan.hao.swing;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;
import java.util.TooManyListenersException;

public class JswFileDropLabel extends JLabel {


    private IFileDropLabelCallBack iFileDropLabelCallBack;

    public JswFileDropLabel() {
        initDropTarget();
    }

    public JswFileDropLabel(IFileDropLabelCallBack iFileDropLabelCallBack) {
        this.iFileDropLabelCallBack = iFileDropLabelCallBack;
        initDropTarget();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("File Drop Label");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 200);
            frame.setLocationRelativeTo(null);

            JswFileDropLabel label = new JswFileDropLabel();
            label.setText("Drop files here");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            frame.add(label);

            frame.setVisible(true);
        });
    }

    private void initDropTarget() {
        DropTarget dt = new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, null);
        dt.setActive(true);
        try {
            dt.addDropTargetListener(new DropTargetAdapter() {
                @Override
                public void drop(DropTargetDropEvent dtde) {
                    try {
// 接收拖拽过来的数据
                        Transferable tr = dtde.getTransferable();
                        DataFlavor[] flavors = tr.getTransferDataFlavors();
                        for (DataFlavor flavor : flavors) {
                            if (flavor.isFlavorJavaFileListType()) {
                                dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                                List<File> files = (List<File>) tr.getTransferData(flavor);
                                handleFiles(files);
                                dtde.dropComplete(true);
                                if (iFileDropLabelCallBack != null) {
                                    iFileDropLabelCallBack.dropLabelCallBack(files);
                                }
                                return;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dtde.rejectDrop();
                }
            });
        } catch (TooManyListenersException e) {
            e.printStackTrace();
        }
    }

    private void handleFiles(List<File> files) {
        if (files == null || files.isEmpty()) {
            return;
        }
        for (File file : files) {
// 处理拖拽过来的文件
            System.out.println("File: " + file.getAbsolutePath());
        }
    }

    public interface IFileDropLabelCallBack {
        void dropLabelCallBack(List<File> files);
    }

}