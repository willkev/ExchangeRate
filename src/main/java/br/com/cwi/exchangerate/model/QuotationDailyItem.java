package br.com.cwi.exchangerate.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author Willian Kirschner (willkev@gmail.com)
 */
public class QuotationDailyItem {
    /*
     Moedas do Tipo "A":
     - Para calcular o valor equivalente em US$ (dólar americano), divida o montante na moeda consultada pela 
     respectiva paridade.
     - Para obter o valor em R$ (reais), multiplique o montante na moeda consultada pela respectiva taxa.
     Moedas do Tipo "B":
     - Para calcular o valor equivalente em US$ (dólar americano), multiplique o montante na moeda consultada 
     pela respectiva paridade.
     - Para obter o valor em R$ (reais), multiplique o montante na moeda consultada pela respectiva taxa. 
     */

    public enum Type {

        A,
        B
    }

    private final Long code;
    private final Type type;
    private final String abbreviationName;
    private final BigDecimal buyingRate;
    private final BigDecimal seleRate;
    private final BigDecimal buyingParity;
    private final BigDecimal seleParity;

    public QuotationDailyItem(Long code, Type type, String abbreviationName, BigDecimal buyingRate, BigDecimal seleRate, BigDecimal buyingParity, BigDecimal seleParity) {
        this.code = code;
        this.type = type;
        this.abbreviationName = abbreviationName;
        this.buyingRate = buyingRate;
        this.seleRate = seleRate;
        this.buyingParity = buyingParity;
        this.seleParity = seleParity;
    }

    public Long getCode() {
        return code;
    }

    public Type getType() {
        return type;
    }

    public String getAbbreviationName() {
        return abbreviationName;
    }

    public BigDecimal getBuyingRate() {
        return buyingRate;
    }

    public BigDecimal getSeleRate() {
        return seleRate;
    }

    public BigDecimal getBuyingParity() {
        return buyingParity;
    }

    public BigDecimal getSeleParity() {
        return seleParity;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.code);
        hash = 71 * hash + Objects.hashCode(this.buyingRate);
        hash = 71 * hash + Objects.hashCode(this.seleRate);
        hash = 71 * hash + Objects.hashCode(this.buyingParity);
        hash = 71 * hash + Objects.hashCode(this.seleParity);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final QuotationDailyItem other = (QuotationDailyItem) obj;
        if (!Objects.equals(this.code, other.code)) {
            return false;
        }
        if (!Objects.equals(this.buyingRate, other.buyingRate)) {
            return false;
        }
        if (!Objects.equals(this.seleRate, other.seleRate)) {
            return false;
        }
        if (!Objects.equals(this.buyingParity, other.buyingParity)) {
            return false;
        }
        if (!Objects.equals(this.seleParity, other.seleParity)) {
            return false;
        }
        return true;
    }

}
