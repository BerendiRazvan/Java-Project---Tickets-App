using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MPP_Proiect.domain
{
    class Artist : Entity<long>
    {
        private string nume;
        private DateTime data;
        private Locatie locatie;
        private int bileteDisponibile;
        private int bileteVandute;

        public Artist(string nume, DateTime data, Locatie locatie, int bileteDisponibile, int bileteVandute)
        {
            this.nume = nume;
            this.data = data;
            this.locatie = locatie;
            this.bileteDisponibile = bileteDisponibile;
            this.bileteVandute = bileteVandute;
        }
        public string getNume()
        {
            return nume;
        }

        public void setNume(string nume)
        {
            this.nume = nume;
        }

        public DateTime getData()
        {
            return data;
        }

        public void setData(DateTime data)
        {
            this.data = data;
        }

        public Locatie getLocatie()
        {
            return locatie;
        }

        public void setLocatie(Locatie locatie)
        {
            this.locatie = locatie;
        }

        public int getBileteDisponibile()
        {
            return bileteDisponibile;
        }

        public void setBileteDisponibile(int bileteDisponibile)
        {
            this.bileteDisponibile = bileteDisponibile;
        }

        public int getBileteVandute()
        {
            return bileteVandute;
        }

        public void setBileteVandute(int bileteVandute)
        {
            this.bileteVandute = bileteVandute;
        }


        public override string ToString()
        {
            return "Artist{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", data=" + data +
                ", locatie=" + locatie +
                ", bileteDisponibile=" + bileteDisponibile +
                ", bileteVandute=" + bileteVandute +
                '}';
        }
    }
}
