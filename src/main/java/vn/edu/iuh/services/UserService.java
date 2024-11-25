package vn.edu.iuh.services;

import vn.edu.iuh.models.User;

import java.util.UUID;

public interface UserService {
    User getReference(UUID id);
}
