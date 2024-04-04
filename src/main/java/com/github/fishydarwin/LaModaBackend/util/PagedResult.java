package com.github.fishydarwin.LaModaBackend.util;

import java.util.Collection;

public record PagedResult<T>(Collection<T> result, long size) {
}
