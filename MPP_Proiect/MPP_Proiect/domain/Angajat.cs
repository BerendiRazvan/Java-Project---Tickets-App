using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MPP_Proiect.domain
{
    class Angajat : Entity<long>
    {
        public string nume { get; set; }
        public string prenume { get; set; }
        public string mail { get; set; }
        public string password { get; set; }

        public Angajat(string nume, string prenume, string mail, string password)
        {
            this.nume = nume;
            this.prenume = prenume;
            this.mail = mail;
            this.password = password;
        }


        public override string ToString()
        {
            return "Angajat{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", prenume='" + prenume + '\'' +
                ", mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                '}';
        }

    }
}
