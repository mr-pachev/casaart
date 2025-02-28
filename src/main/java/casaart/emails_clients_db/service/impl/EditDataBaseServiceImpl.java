package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.entity.Client;
import casaart.emails_clients_db.repository.ClientRepository;
import casaart.emails_clients_db.service.EditDataBaseService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EditDataBaseServiceImpl implements EditDataBaseService {
    private final ClientRepository clientRepository;
    public EditDataBaseServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // remove duplicate clients
    @Override
    @Transactional
    public void removeDuplicateClients() {

        List<Client> allClients = clientRepository.findAll();
        allClients.stream()
                .collect(Collectors.groupingBy(c -> c.getFirstName() + "|" + c.getLastName() + "|" + c.getEmail()))
                .values()
                .forEach(duplicates -> {
                    if (duplicates.size() > 1) {
                        duplicates.stream().skip(1).forEach(clientRepository::delete);
                    }
                });
    }

    // remove duplicate clients same email
    @Override
    @Transactional
    public void removeDuplicateClientsSameEmail() {
        clientRepository.removeDuplicateClientsEmail();
    }

    // remove clients who email ended on @guest.booking.com
    @Override
    @Transactional
    public void removedClientsWithFalseEmail() {
        List<Client> allClients = clientRepository.findAll();

        allClients.stream()
                .filter(client -> client.getEmail() == null || client.getEmail().endsWith("@guest.booking.com"))
                .forEach(clientRepository::delete);
    }

    // update all client names
    @Override
    public void updateAllClientNames() {
        List<Client> allClients = clientRepository.findAll(); // Извличаме всички клиенти

        for (Client client : allClients) {
            client.setFirstName(formatName(client.getFirstName()));
            client.setMiddleName(formatName(client.getMiddleName()));
            client.setLastName(formatName(client.getLastName()));
        }

        clientRepository.saveAll(allClients); // Запазваме обновените клиенти
        System.out.println("UPDATED ARE --< " + allClients.size() + " >-- CLIENTS.");
    }

    // Обработка на имената
    private String formatName(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }
        name = name.toLowerCase();
        String[] words = name.split("\\s+");
        StringBuilder formattedName = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                formattedName.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
            }
        }
        return formattedName.toString().trim();
    }
}

