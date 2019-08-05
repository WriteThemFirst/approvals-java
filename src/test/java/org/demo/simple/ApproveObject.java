/*
 * Approvals-Java - Approval testing library for Java. Alleviates the burden of hand-writing assertions.
 * Copyright © 2018 Write Them First!
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.demo.simple;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static com.github.writethemfirst.approvals.Approvals.verify;

class ApproveObject {
    Person person = new Person("toto", new Address("Lille", "59000", "5 rue de Fives"));

    @Test
    void verifySimplePojo() {
        verify(JSON.toJSONString(person, true));
    }

    @Test
    void verifySimpleMap() {
        Map<String, Person> map = Collections.singletonMap("1223", person);
        verify(JSON.toJSONString(map, true));
    }

    static class Person {
        final String name;
        final Address address;

        Person(final String name, final Address address) {
            this.name = name;
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public Address getAddress() {
            return address;
        }
    }

    static class Address {
        final String city;
        final String postalCode;
        final String street;

        Address(final String city, final String postalCode, final String street) {
            this.city = city;
            this.postalCode = postalCode;
            this.street = street;
        }

        public String getCity() {
            return city;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public String getStreet() {
            return street;
        }
    }
}
