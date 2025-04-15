package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.dto.OrderDTO;

import java.util.List;

public interface OrderService {

    // get current orders by client id
    List<OrderDTO> currentOrdersByClientId(long id);

    // get order by id
    OrderDTO getOrderById(long orderId);

    // check is exist order
    boolean isExistOrder(OrderDTO orderDTO);

    // add order
    void addOrder(OrderDTO orderDTO, long clientId);

    // edit order
    void editOrder(OrderDTO orderDTO);

    // delete order
    void removeOrder(long orderId);
}
