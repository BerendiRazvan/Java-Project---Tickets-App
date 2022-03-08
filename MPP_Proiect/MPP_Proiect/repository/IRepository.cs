﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using MPP_Proiect.domain;

namespace MPP_Proiect.repository
{
    interface IRepository<ID, E> where E : Entity<ID> //E extends Entity<ID>
    {

        /**
         * @param id -the id of the entity to be returned
         *           id must not be null
         * @return the entity with the specified id
         * or null - if there is no entity with the given id
         * @throws IllegalArgumentException if id is null.
         */

        E findOne(ID id);

        /**
         * @return all entities
         */
        List<E> findAll();

        /**
         * @param entity entity must be not null
         * @return null- if the given entity is saved
         * otherwise returns the entity (id already exists)
         * @throws IllegalArgumentException if the given entity is null.     *
         */
        E save(E entity);


        /**
         * removes the entity with the specified id
         *
         * @param id id must be not null
         * @return the removed entity or null if there is no entity with the given id
         * @throws IllegalArgumentException if the given id is null.
         */
        E delete(ID id);

        /**
         * @param entity entity must not be null
         * @return null - if the entity is updated,
         * otherwise  returns the entity  - (e.g id does not exist).
         * @throws IllegalArgumentException if the given entity is null.
         */
        E update(E entity);


    }
}
