package org.yifan.hao.swing;

import javax.swing.*;

/**
 * 添加右键菜单
 * <code>JPanel panel = new JPanel();
 * *  panel.setComponentPopupMenu(new JPopupMenus().addMenu("复制", new JPopupMenus.JPopupMenusClick() {
 * *            @Override
 * *            public void click(String menuName) {
 * *                WinUtils.setSysClipboardText(jTextAreaDrag.getText());
 * *            }
 * *        }).addMenu("粘贴", new JPopupMenus.JPopupMenusClick() {
 * *            @Override
 * *            public void click(String menuName) {
 * *                jTextAreaDrag.setText(WinUtils.getSysClipboardText());
 * *            }
 * *        }));
 * </code>
 */
public class JswPopupMenus extends JPopupMenu {

    public JswPopupMenus() {
    }

    public JswPopupMenus(JPopupMenusClicks jPopupMenusClick, String... btns) {
        for (int i = 0; i < btns.length; i++) {
            int index = i;
            JMenuItem menuItem1 = new JMenuItem(btns[index]);
            menuItem1.addActionListener(e -> {
                jPopupMenusClick.click(index, btns[index]);
            });
            add(menuItem1);
        }
    }

    public JswPopupMenus addMenu(String itemMenuName, JPopupMenusClick jPopupMenusClick) {
        JMenuItem menuItem1 = new JMenuItem(itemMenuName);
        menuItem1.addActionListener(e -> {
            jPopupMenusClick.click(itemMenuName);
        });
        add(menuItem1);
        return this;
    }

    public interface JPopupMenusClicks {
        void click(int menuIndex, String menuName);
    }

    public interface JPopupMenusClick {
        void click(String menuName);
    }
}
