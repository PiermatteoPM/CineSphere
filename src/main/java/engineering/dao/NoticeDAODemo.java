package engineering.dao;

import model.Client;
import model.Notice;

import java.util.ArrayList;
import java.util.List;

public class NoticeDAODemo implements NoticeDAO {

    private static List<Notice> notices = new ArrayList<>();

    @Override
    public void addNotice(Notice notice) {
        notices.add(notice);
    }

    @Override
    public void deleteNotice(Notice notice) {
        notices.removeIf(n -> n.getTitle().equals(notice.getTitle()) && n.getEmail().equals(notice.getEmail()));
    }

    @Override
    public List<Notice> retrieveNotice(Client user) {
        return notices.stream()
                .filter(n -> n.getEmail().equals(user.getEmail()))
                .toList();
    }
}
