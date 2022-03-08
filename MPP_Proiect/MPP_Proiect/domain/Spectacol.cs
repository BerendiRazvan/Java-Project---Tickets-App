using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MPP_Proiect.domain
{
    class Spectacol : Entity<long>
    {
        public string numeArtist { get; set; }
        public DateTime data { get; set; }
        public Locatie locatie { get; set; }
        public int bileteDisponibile { get; set; }
        public int bileteVandute { get; set; }

        public Spectacol(string numeArtist, DateTime data, Locatie locatie, int bileteDisponibile, int bileteVandute)
        {
            this.numeArtist = numeArtist;
            this.data = data;
            this.locatie = locatie;
            this.bileteDisponibile = bileteDisponibile;
            this.bileteVandute = bileteVandute;
        }


        public override string ToString()
        {
            return "Spectacol{" +
                "id=" + id +
                ", numeArtist='" + numeArtist + '\'' +
                ", data=" + data +
                ", locatie=" + locatie +
                ", bileteDisponibile=" + bileteDisponibile +
                ", bileteVandute=" + bileteVandute +
                '}';
        }
    }
}
