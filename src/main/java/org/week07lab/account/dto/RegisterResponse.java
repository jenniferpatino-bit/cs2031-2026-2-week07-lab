package org.week07lab.account.dto;

/**
 * Solo expone el id del usuario creado: nunca la entidad ni la contrasena.
 */
public record RegisterResponse(Long id) {
}
