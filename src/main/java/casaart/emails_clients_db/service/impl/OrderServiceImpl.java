package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.OrderDTO;
import casaart.emails_clients_db.model.entity.Client;
import casaart.emails_clients_db.model.entity.Order;
import casaart.emails_clients_db.repository.ClientRepository;
import casaart.emails_clients_db.repository.OrderRepository;
import casaart.emails_clients_db.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ModelMapper mapper;

    public OrderServiceImpl(OrderRepository orderRepository, ClientRepository clientRepository, ModelMapper mapper) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.mapper = mapper;
    }

    // get current orders by client id
    @Override
    public List<OrderDTO> currentOrdersByClientId(long id) {
        List<Order> orderList = orderRepository.findAllByClientId(id);
        List<OrderDTO> orderDTOS = new ArrayList<>();

        for (Order order : orderList) {
            orderDTOS.add(mapper.map(order, OrderDTO.class));
        }

        return orderDTOS;
    }

    // get order by id
    @Override
    public OrderDTO getOrderById(long orderId) {
        Order order = orderRepository.findById(orderId).get();
        OrderDTO orderDTO = mapper.map(order, OrderDTO.class);

        return orderDTO;
    }

    // check is exist order
    @Override
    public boolean isExistOrder(OrderDTO orderDTO) {
        return orderRepository.findByNumber(orderDTO.getNumber()).isPresent();
    }

    // add order
    @Override
    public void addOrder(OrderDTO orderDTO, long clientId) {
        Client client = clientRepository.findById(clientId);
        Order order = new Order();
        order.setNumber(orderDTO.getNumber());
        order.setYear(orderDTO.getYear());
        order.setClient(client);
        order.setFolderPath(orderDTO.getFolderPath());

        order.setClient(client);

        orderRepository.save(order);
    }

    // edit order
    @Override
    public void editOrder(OrderDTO orderDTO) {
        Order order = mapper.map(orderDTO, Order.class);
        Client client = clientRepository.findById(orderDTO.getClientId());

        order.setClient(client);

        orderRepository.save(order);
    }

    // delete order
    @Override
    public void removeOrder(long orderId) {
        Order order = orderRepository.findById(orderId).get();
        Client client = order.getClient();

        // Премахване от клиента
        client.getOrders().remove(order);

        // Изчистване на референцията в order
        order.setClient(null);

        // Записване на клиента
        clientRepository.save(client);

        // Изтриване на поръчката
        orderRepository.save(order);
        orderRepository.delete(order);
    }
}
