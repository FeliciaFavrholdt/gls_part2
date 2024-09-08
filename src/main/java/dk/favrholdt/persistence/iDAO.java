package dk.favrholdt.persistence;

public interface iDAO<T> {
    T create(T t);
    T findById(Long id);
    T findByTrackingNumber(String trackingNumber);
    T update(T t);
    boolean delete(T t);
}
