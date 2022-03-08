using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MPP_Proiect.domain
{
    class Angajat : Entity<long>
    {
        private string nume;
        private string prenume;
        private string mail;
        private string password;

        public Angajat(string nume, string prenume, string mail, string password)
        {
            this.nume = nume;
            this.prenume = prenume;
            this.mail = mail;
            this.password = password;
        }

        public string getNume()
        {
            return nume;
        }

        public void setNume(string nume)
        {
            this.nume = nume;
        }

        public string getPrenume()
        {
            return prenume;
        }

        public void setPrenume(string prenume)
        {
            this.prenume = prenume;
        }

        public string getMail()
        {
            return mail;
        }

        public void setMail(string mail)
        {
            this.mail = mail;
        }

        public string getPassword()
        {
            return password;
        }

        public void setPassword(string password)
        {
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
