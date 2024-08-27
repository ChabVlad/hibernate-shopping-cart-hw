package mate.academy.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.dao.impl.ShoppingCartDaoImpl;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Inject
    private ShoppingCartDao shoppingCartDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket ticket = new Ticket();
        ticket.setMovieSession(movieSession);
        ticket.setUser(user);
        ShoppingCart shoppingCart = getByUser(user);
        shoppingCart.getTickets()
            .add(ticket);
        shoppingCartDao.update(shoppingCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find shopping cart by user: " + user));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        shoppingCartDao = new ShoppingCartDaoImpl();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.getTickets().clear();
        shoppingCartDao.update(shoppingCart);
    }
}
