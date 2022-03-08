using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MPP_Proiect.domain
{
    class Entity<ID> //implements Serialization?
    {
        protected ID id;

        public override bool Equals(object obj)
        {
            return base.Equals(obj);
        }

        public override int GetHashCode()
        {
            return base.GetHashCode();
        }

        public ID getId()
        {
            return id;
        }

        public void setId(ID id)
        {
            this.id = id;
        }

        public override string ToString()
        {
            return "Entity{" +
                "id=" + id +
                '}';
        }
    }
}
