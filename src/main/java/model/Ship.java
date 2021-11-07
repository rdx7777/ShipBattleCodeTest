package model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Ship {

    private String name;
    private List<Square> shipSquares;
    private Integer squaresNumber;
}
