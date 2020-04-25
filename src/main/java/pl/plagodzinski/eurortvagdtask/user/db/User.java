package pl.plagodzinski.eurortvagdtask.user.db;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity(name="User")
public class User {

    @Id
    @GenericGenerator(name = "assigned", strategy = "assigned")
    @GeneratedValue(generator = "assigned")
    @Column(name = "PESEL")
    private String pesel;

    @Column(name = "NAME", length = 30, nullable = false)
    private String name;

    @Column(name = "SURNAME", length = 30, nullable = false)
    private String surname;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<MoneyAccount> moneyAccountList = new ArrayList<>();

    public String getPesel() {
        return this.pesel;
    }

    public void setPesel(final String pesel) {
        this.pesel = pesel;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(final String surename) {
        this.surname = surename;
    }

    public List<MoneyAccount> getMoneyAccountList() {
        return this.moneyAccountList;
    }

    public void setMoneyAccountList(final List<MoneyAccount> moneyAmountList) {
        this.moneyAccountList = moneyAmountList;
    }
}

